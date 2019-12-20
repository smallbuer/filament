/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.filament.gltfio;

public class Manipulator {
    private Mode mMode;

    public enum Mode { ORBIT, MAP };

    public enum Fov { VERTICAL, HORIZONTAL };

    /**
     * User-controlled properties that are never computed or changed by the manipulator.
     */
    public class Properties {
        public int viewport[] = new int[2];           //! Width and height of the viewing area
        public float targetPosition[] = new float[3]; //! World-space position of interest, defaults to (0,0,0)
        public float upVector[] = new float[3];       //! Orientation for the home position, defaults to (0,1,0)
        public float zoomSpeed;                       //! Multiplied with scroll delta, defaults to 0.01

        // Orbit mode properties
        public float orbitHomePosition[] = new float[3]; //! Initial eye position in world space, defaults to (0,0,1)
        public float orbitSpeed[] = new float[2];        //! Multiplied with viewport delta, defaults to 0.01

        // Map mode properties
        public Fov fovDirection;                   //! The FOV axis that's held constant when the viewport changes
        public float fovDegrees;                   //! The full FOV (not the half-angle)
        public float farPlane;                     //! The distance to the far plane
        public float mapExtent[] = new float[2];   //! The ground plane size used to compute the home position
        public float mapMinDistance;               //! Constrains the zoom-in level

        // Raycast properties
        public float groundPlane[] = new float[4]; //! Plane equation used as a raycast fallback

        // TODO: add raycastCallback
    };

    /**
     * Creates a new camera manipulator, either ORBIT or MAP.
     */
    public static Manipulator create(Mode mode, Properties props) {
        return null;
    }

    /**
     * Gets the immutable mode of the manipulator.
     */
    public Mode getMode() { return mMode; }

    /**
     * Changes the state of the manipulator, for example the point of interest, viewport size,
     * or the position of the ground plane.
     *
     * \see Properties
     */
    public void setProperties(Properties props) {}

    /**
     * Retrieves all directly-settable state of the manipulator.
     *
     * see Properties
     */
    public Properties getProperties() {}

    /**
     * Gets the current orthonormal basis; this is usually called once per frame.
     */
    public void getLookAt(
            @NonNull @Size(min = 3) float[] eyePosition,
            @NonNull @Size(min = 3) float[] targetPosition,
            @NonNull @Size(min = 3) float[] upward) {}

    /**
     * Given a viewport coordinate, picks a point in the ground plane, or in the actual scene if the
     * raycast callback was provided.
     */
    @Nullable @Size(min = 3)
    public float[] raycast(int x, int y) {
        return null;
    }

    /**
     * Starts a grabbing session (i.e. the user is dragging around in the viewport).
     *
     * This starts a panning session in MAP mode, and start either rotating or strafing in ORBIT.
     *
     * @param x X-coordinate for point of interest in viewport space
     * @param y Y-coordinate for point of interest in viewport space
     * @param strafe ORBIT mode only; if true, starts a translation rather than a rotation.
     */
    public void grabBegin(int x, int y, Boolean strafe) {}

    /**
     * Updates a grabbing session.
     *
     * This must be called at least once between grabBegin / grabEnd to dirty the camera.
     */
    public void grabUpdate(int x, int y) {}

    /**
     * Ends a grabbing session.
     */
    public void grabEnd(int x, int y) {}

    /**
     * Dollys the camera along the viewing direction.
     *
     * @param x X-coordinate for point of interest in viewport space
     * @param y Y-coordinate for point of interest in viewport space
     * @param scrolldelta Positive means "zoom in", negative means "zoom out"
     */
    public void zoom(int x, int y, float scrolldelta) {}

    /**
     * Gets a handle that can be used to reset the manipulator back to its current position.
     *
     * \see jumpToBookmark
     */
    public Bookmark getCurrentBookmark() {
        return null;
    }

    /**
     * Gets a handle that can be used to reset the manipulator back to its home position.
     *
     * see jumpToBookmark
     */
    public Bookmark getHomeBookmark() {
        return null;
    }

    /**
     * Sets the manipulator position and orientation back to a stashed state.
     *
     * \see getCurrentBookmark, getHomeBookmark
     */
    public void jumpToBookmark(Bookmark bookmark) {
    }
}
