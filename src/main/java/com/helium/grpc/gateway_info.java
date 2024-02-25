// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: service/iot_config.proto

// Protobuf Java Version: 3.25.1
package com.helium.grpc;

/**
 * Protobuf type {@code helium.iot_config.gateway_info}
 */
public final class gateway_info extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:helium.iot_config.gateway_info)
    gateway_infoOrBuilder {
private static final long serialVersionUID = 0L;
  // Use gateway_info.newBuilder() to construct.
  private gateway_info(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private gateway_info() {
    address_ = com.google.protobuf.ByteString.EMPTY;
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new gateway_info();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.helium.grpc.IotConfig.internal_static_helium_iot_config_gateway_info_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.helium.grpc.IotConfig.internal_static_helium_iot_config_gateway_info_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.helium.grpc.gateway_info.class, com.helium.grpc.gateway_info.Builder.class);
  }

  private int bitField0_;
  public static final int ADDRESS_FIELD_NUMBER = 1;
  private com.google.protobuf.ByteString address_ = com.google.protobuf.ByteString.EMPTY;
  /**
   * <pre>
   * The public key binary address and on-chain identity of the gateway
   * </pre>
   *
   * <code>bytes address = 1;</code>
   * @return The address.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getAddress() {
    return address_;
  }

  public static final int IS_FULL_HOTSPOT_FIELD_NUMBER = 2;
  private boolean isFullHotspot_ = false;
  /**
   * <pre>
   * Whether or not the hotspot participates in PoC or only transfers data
   * </pre>
   *
   * <code>bool is_full_hotspot = 2;</code>
   * @return The isFullHotspot.
   */
  @java.lang.Override
  public boolean getIsFullHotspot() {
    return isFullHotspot_;
  }

  public static final int METADATA_FIELD_NUMBER = 3;
  private com.helium.grpc.gateway_metadata metadata_;
  /**
   * <pre>
   * The gateway's metadata as recorded on the blockchain
   * </pre>
   *
   * <code>.helium.iot_config.gateway_metadata metadata = 3;</code>
   * @return Whether the metadata field is set.
   */
  @java.lang.Override
  public boolean hasMetadata() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   * <pre>
   * The gateway's metadata as recorded on the blockchain
   * </pre>
   *
   * <code>.helium.iot_config.gateway_metadata metadata = 3;</code>
   * @return The metadata.
   */
  @java.lang.Override
  public com.helium.grpc.gateway_metadata getMetadata() {
    return metadata_ == null ? com.helium.grpc.gateway_metadata.getDefaultInstance() : metadata_;
  }
  /**
   * <pre>
   * The gateway's metadata as recorded on the blockchain
   * </pre>
   *
   * <code>.helium.iot_config.gateway_metadata metadata = 3;</code>
   */
  @java.lang.Override
  public com.helium.grpc.gateway_metadataOrBuilder getMetadataOrBuilder() {
    return metadata_ == null ? com.helium.grpc.gateway_metadata.getDefaultInstance() : metadata_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!address_.isEmpty()) {
      output.writeBytes(1, address_);
    }
    if (isFullHotspot_ != false) {
      output.writeBool(2, isFullHotspot_);
    }
    if (((bitField0_ & 0x00000001) != 0)) {
      output.writeMessage(3, getMetadata());
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!address_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(1, address_);
    }
    if (isFullHotspot_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(2, isFullHotspot_);
    }
    if (((bitField0_ & 0x00000001) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(3, getMetadata());
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.helium.grpc.gateway_info)) {
      return super.equals(obj);
    }
    com.helium.grpc.gateway_info other = (com.helium.grpc.gateway_info) obj;

    if (!getAddress()
        .equals(other.getAddress())) return false;
    if (getIsFullHotspot()
        != other.getIsFullHotspot()) return false;
    if (hasMetadata() != other.hasMetadata()) return false;
    if (hasMetadata()) {
      if (!getMetadata()
          .equals(other.getMetadata())) return false;
    }
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + ADDRESS_FIELD_NUMBER;
    hash = (53 * hash) + getAddress().hashCode();
    hash = (37 * hash) + IS_FULL_HOTSPOT_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getIsFullHotspot());
    if (hasMetadata()) {
      hash = (37 * hash) + METADATA_FIELD_NUMBER;
      hash = (53 * hash) + getMetadata().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.helium.grpc.gateway_info parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.helium.grpc.gateway_info parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.helium.grpc.gateway_info parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.helium.grpc.gateway_info parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.helium.grpc.gateway_info parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.helium.grpc.gateway_info parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.helium.grpc.gateway_info parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.helium.grpc.gateway_info parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static com.helium.grpc.gateway_info parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static com.helium.grpc.gateway_info parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.helium.grpc.gateway_info parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.helium.grpc.gateway_info parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.helium.grpc.gateway_info prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code helium.iot_config.gateway_info}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:helium.iot_config.gateway_info)
      com.helium.grpc.gateway_infoOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.helium.grpc.IotConfig.internal_static_helium_iot_config_gateway_info_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.helium.grpc.IotConfig.internal_static_helium_iot_config_gateway_info_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.helium.grpc.gateway_info.class, com.helium.grpc.gateway_info.Builder.class);
    }

    // Construct using com.helium.grpc.gateway_info.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
        getMetadataFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      address_ = com.google.protobuf.ByteString.EMPTY;
      isFullHotspot_ = false;
      metadata_ = null;
      if (metadataBuilder_ != null) {
        metadataBuilder_.dispose();
        metadataBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.helium.grpc.IotConfig.internal_static_helium_iot_config_gateway_info_descriptor;
    }

    @java.lang.Override
    public com.helium.grpc.gateway_info getDefaultInstanceForType() {
      return com.helium.grpc.gateway_info.getDefaultInstance();
    }

    @java.lang.Override
    public com.helium.grpc.gateway_info build() {
      com.helium.grpc.gateway_info result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.helium.grpc.gateway_info buildPartial() {
      com.helium.grpc.gateway_info result = new com.helium.grpc.gateway_info(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.helium.grpc.gateway_info result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.address_ = address_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.isFullHotspot_ = isFullHotspot_;
      }
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.metadata_ = metadataBuilder_ == null
            ? metadata_
            : metadataBuilder_.build();
        to_bitField0_ |= 0x00000001;
      }
      result.bitField0_ |= to_bitField0_;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.helium.grpc.gateway_info) {
        return mergeFrom((com.helium.grpc.gateway_info)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.helium.grpc.gateway_info other) {
      if (other == com.helium.grpc.gateway_info.getDefaultInstance()) return this;
      if (other.getAddress() != com.google.protobuf.ByteString.EMPTY) {
        setAddress(other.getAddress());
      }
      if (other.getIsFullHotspot() != false) {
        setIsFullHotspot(other.getIsFullHotspot());
      }
      if (other.hasMetadata()) {
        mergeMetadata(other.getMetadata());
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 10: {
              address_ = input.readBytes();
              bitField0_ |= 0x00000001;
              break;
            } // case 10
            case 16: {
              isFullHotspot_ = input.readBool();
              bitField0_ |= 0x00000002;
              break;
            } // case 16
            case 26: {
              input.readMessage(
                  getMetadataFieldBuilder().getBuilder(),
                  extensionRegistry);
              bitField0_ |= 0x00000004;
              break;
            } // case 26
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private com.google.protobuf.ByteString address_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <pre>
     * The public key binary address and on-chain identity of the gateway
     * </pre>
     *
     * <code>bytes address = 1;</code>
     * @return The address.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString getAddress() {
      return address_;
    }
    /**
     * <pre>
     * The public key binary address and on-chain identity of the gateway
     * </pre>
     *
     * <code>bytes address = 1;</code>
     * @param value The address to set.
     * @return This builder for chaining.
     */
    public Builder setAddress(com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      address_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * The public key binary address and on-chain identity of the gateway
     * </pre>
     *
     * <code>bytes address = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearAddress() {
      bitField0_ = (bitField0_ & ~0x00000001);
      address_ = getDefaultInstance().getAddress();
      onChanged();
      return this;
    }

    private boolean isFullHotspot_ ;
    /**
     * <pre>
     * Whether or not the hotspot participates in PoC or only transfers data
     * </pre>
     *
     * <code>bool is_full_hotspot = 2;</code>
     * @return The isFullHotspot.
     */
    @java.lang.Override
    public boolean getIsFullHotspot() {
      return isFullHotspot_;
    }
    /**
     * <pre>
     * Whether or not the hotspot participates in PoC or only transfers data
     * </pre>
     *
     * <code>bool is_full_hotspot = 2;</code>
     * @param value The isFullHotspot to set.
     * @return This builder for chaining.
     */
    public Builder setIsFullHotspot(boolean value) {

      isFullHotspot_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * Whether or not the hotspot participates in PoC or only transfers data
     * </pre>
     *
     * <code>bool is_full_hotspot = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearIsFullHotspot() {
      bitField0_ = (bitField0_ & ~0x00000002);
      isFullHotspot_ = false;
      onChanged();
      return this;
    }

    private com.helium.grpc.gateway_metadata metadata_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.helium.grpc.gateway_metadata, com.helium.grpc.gateway_metadata.Builder, com.helium.grpc.gateway_metadataOrBuilder> metadataBuilder_;
    /**
     * <pre>
     * The gateway's metadata as recorded on the blockchain
     * </pre>
     *
     * <code>.helium.iot_config.gateway_metadata metadata = 3;</code>
     * @return Whether the metadata field is set.
     */
    public boolean hasMetadata() {
      return ((bitField0_ & 0x00000004) != 0);
    }
    /**
     * <pre>
     * The gateway's metadata as recorded on the blockchain
     * </pre>
     *
     * <code>.helium.iot_config.gateway_metadata metadata = 3;</code>
     * @return The metadata.
     */
    public com.helium.grpc.gateway_metadata getMetadata() {
      if (metadataBuilder_ == null) {
        return metadata_ == null ? com.helium.grpc.gateway_metadata.getDefaultInstance() : metadata_;
      } else {
        return metadataBuilder_.getMessage();
      }
    }
    /**
     * <pre>
     * The gateway's metadata as recorded on the blockchain
     * </pre>
     *
     * <code>.helium.iot_config.gateway_metadata metadata = 3;</code>
     */
    public Builder setMetadata(com.helium.grpc.gateway_metadata value) {
      if (metadataBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        metadata_ = value;
      } else {
        metadataBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * The gateway's metadata as recorded on the blockchain
     * </pre>
     *
     * <code>.helium.iot_config.gateway_metadata metadata = 3;</code>
     */
    public Builder setMetadata(
        com.helium.grpc.gateway_metadata.Builder builderForValue) {
      if (metadataBuilder_ == null) {
        metadata_ = builderForValue.build();
      } else {
        metadataBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * The gateway's metadata as recorded on the blockchain
     * </pre>
     *
     * <code>.helium.iot_config.gateway_metadata metadata = 3;</code>
     */
    public Builder mergeMetadata(com.helium.grpc.gateway_metadata value) {
      if (metadataBuilder_ == null) {
        if (((bitField0_ & 0x00000004) != 0) &&
          metadata_ != null &&
          metadata_ != com.helium.grpc.gateway_metadata.getDefaultInstance()) {
          getMetadataBuilder().mergeFrom(value);
        } else {
          metadata_ = value;
        }
      } else {
        metadataBuilder_.mergeFrom(value);
      }
      if (metadata_ != null) {
        bitField0_ |= 0x00000004;
        onChanged();
      }
      return this;
    }
    /**
     * <pre>
     * The gateway's metadata as recorded on the blockchain
     * </pre>
     *
     * <code>.helium.iot_config.gateway_metadata metadata = 3;</code>
     */
    public Builder clearMetadata() {
      bitField0_ = (bitField0_ & ~0x00000004);
      metadata_ = null;
      if (metadataBuilder_ != null) {
        metadataBuilder_.dispose();
        metadataBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <pre>
     * The gateway's metadata as recorded on the blockchain
     * </pre>
     *
     * <code>.helium.iot_config.gateway_metadata metadata = 3;</code>
     */
    public com.helium.grpc.gateway_metadata.Builder getMetadataBuilder() {
      bitField0_ |= 0x00000004;
      onChanged();
      return getMetadataFieldBuilder().getBuilder();
    }
    /**
     * <pre>
     * The gateway's metadata as recorded on the blockchain
     * </pre>
     *
     * <code>.helium.iot_config.gateway_metadata metadata = 3;</code>
     */
    public com.helium.grpc.gateway_metadataOrBuilder getMetadataOrBuilder() {
      if (metadataBuilder_ != null) {
        return metadataBuilder_.getMessageOrBuilder();
      } else {
        return metadata_ == null ?
            com.helium.grpc.gateway_metadata.getDefaultInstance() : metadata_;
      }
    }
    /**
     * <pre>
     * The gateway's metadata as recorded on the blockchain
     * </pre>
     *
     * <code>.helium.iot_config.gateway_metadata metadata = 3;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.helium.grpc.gateway_metadata, com.helium.grpc.gateway_metadata.Builder, com.helium.grpc.gateway_metadataOrBuilder> 
        getMetadataFieldBuilder() {
      if (metadataBuilder_ == null) {
        metadataBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.helium.grpc.gateway_metadata, com.helium.grpc.gateway_metadata.Builder, com.helium.grpc.gateway_metadataOrBuilder>(
                getMetadata(),
                getParentForChildren(),
                isClean());
        metadata_ = null;
      }
      return metadataBuilder_;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:helium.iot_config.gateway_info)
  }

  // @@protoc_insertion_point(class_scope:helium.iot_config.gateway_info)
  private static final com.helium.grpc.gateway_info DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.helium.grpc.gateway_info();
  }

  public static com.helium.grpc.gateway_info getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<gateway_info>
      PARSER = new com.google.protobuf.AbstractParser<gateway_info>() {
    @java.lang.Override
    public gateway_info parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<gateway_info> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<gateway_info> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.helium.grpc.gateway_info getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

