/*
 * Copyright (C) 2018 The Android Open Source Project
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

#ifndef IMAGE_KTXBUNDLE_H
#define IMAGE_KTXBUNDLE_H

#include <cstdint>

namespace image {

struct KtxInfo {
    uint32_t endianness;
    uint32_t glType;
    uint32_t glTypeSize;
    uint32_t glFormat;
    uint32_t glInternalFormat;
    uint32_t glBaseInternalFormat;
    uint32_t pixelWidth;
    uint32_t pixelHeight;
    uint32_t pixelDepth;
};

struct KtxBlobIndex {
    uint32_t mipLevel;
    uint32_t arrayIndex;
    uint32_t cubeFace;
};

struct KtxBlobList;

/**
 * KtxBundle is a structured set of opaque data blobs that can be passed straight to the GPU, such
 * that a single bundle corresponds to a single texture object. It is well suited for storing
 * block-compressed texture data.
 *
 * One bundle may be comprised of several mipmap levels, cubemap faces, and array elements. The
 * number of blobs is immutable, and is determined as follows.
 * 
 *     blob_count = mip_count * array_length * (cubemap ? 6 : 1)
 *
 * Bundles can be quickly serialized to a certain file format (see below link), but this class lives
 * in the image lib rather than imageio because it has no dependencies, and does not support CPU
 * decoding.
 * 
 *     https://www.khronos.org/opengles/sdk/tools/KTX/file_format_spec/
 *
 * WARNING: for now, this class discards the arbitrary key/value data that can be embedded in KTX.
 */
class KtxBundle {
public:

    ~KtxBundle();

    /**
     * Creates a hierarchy of empty texture blobs, to be filled later via setBlob().
     */
    KtxBundle(uint32_t numMipLevels, uint32_t arrayLength, bool isCubemap);

    /**
     * Creates a new bundle by deserializing the given data.
     *
     * Typically, this constructor is used to consume the contents of a KTX file.
     */
    KtxBundle(uint8_t const* bytes, uint32_t nbytes);

    /**
     * Serializes the bundle into the given target memory. Returns false if there's not enough
     * memory.
     *
     * Typically, this method is used to write out the contents of a KTX file.
     */
    bool serialize(uint8_t* destination, uint32_t numBytes) const;

    /**
     * Computes the size (in bytes) of the serialized bundle.
     */
    uint32_t getSerializedLength() const;

    /**
     * Gets or sets information about the texture object, such as format and type.
     */
    KtxInfo const& getInfo() const { return mInfo; }
    KtxInfo& info() { return mInfo; }

    /**
     * Gets the number of miplevels (this is never zero).
     */
    uint32_t getNumMipLevels() const { return mNumMipLevels; }

    /**
     * Gets the number of array elements (this is never zero).
     */
    uint32_t getArrayLength() const { return mArrayLength; }

    /**
     * Returns whether or not this is a cubemap.
     */
    bool isCubemap() const { return mNumCubeFaces > 1; }

    /**
     * Retrieves a weak reference to a given data blob. Returns false if the given blob index is out
     * of bounds, or if the blob at the given index is empty.
     */
    bool getBlob(KtxBlobIndex index, uint8_t** data, uint32_t* size) const;

    /**
     * Copies the given data into the blob at the given index, replacing whatever is already there.
     * Returns false if the given blob index is out of bounds.
     */
    bool setBlob(KtxBlobIndex index, uint8_t const* data, uint32_t size);

private:
    image::KtxInfo mInfo = {};
    uint32_t mNumMipLevels;
    uint32_t mArrayLength;
    uint32_t mNumCubeFaces;
    KtxBlobList* mBlobs = nullptr;
};

} // namespace image

#endif /* IMAGE_KTXBUNDLE_H */
