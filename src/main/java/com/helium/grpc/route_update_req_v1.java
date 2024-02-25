// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: service/iot_config.proto

// Protobuf Java Version: 3.25.1
package com.helium.grpc;

/**
 * Protobuf type {@code helium.iot_config.route_update_req_v1}
 */
public final class route_update_req_v1 extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:helium.iot_config.route_update_req_v1)
    route_update_req_v1OrBuilder {
private static final long serialVersionUID = 0L;
  // Use route_update_req_v1.newBuilder() to construct.
  private route_update_req_v1(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private route_update_req_v1() {
    signature_ = com.google.protobuf.ByteString.EMPTY;
    signer_ = com.google.protobuf.ByteString.EMPTY;
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new route_update_req_v1();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.helium.grpc.IotConfig.internal_static_helium_iot_config_route_update_req_v1_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.helium.grpc.IotConfig.internal_static_helium_iot_config_route_update_req_v1_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.helium.grpc.route_update_req_v1.class, com.helium.grpc.route_update_req_v1.Builder.class);
  }

  private int bitField0_;
  public static final int ROUTE_FIELD_NUMBER = 1;
  private com.helium.grpc.route_v1 route_;
  /**
   * <code>.helium.iot_config.route_v1 route = 1;</code>
   * @return Whether the route field is set.
   */
  @java.lang.Override
  public boolean hasRoute() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   * <code>.helium.iot_config.route_v1 route = 1;</code>
   * @return The route.
   */
  @java.lang.Override
  public com.helium.grpc.route_v1 getRoute() {
    return route_ == null ? com.helium.grpc.route_v1.getDefaultInstance() : route_;
  }
  /**
   * <code>.helium.iot_config.route_v1 route = 1;</code>
   */
  @java.lang.Override
  public com.helium.grpc.route_v1OrBuilder getRouteOrBuilder() {
    return route_ == null ? com.helium.grpc.route_v1.getDefaultInstance() : route_;
  }

  public static final int TIMESTAMP_FIELD_NUMBER = 2;
  private long timestamp_ = 0L;
  /**
   * <pre>
   * in milliseconds since unix epoch
   * </pre>
   *
   * <code>uint64 timestamp = 2;</code>
   * @return The timestamp.
   */
  @java.lang.Override
  public long getTimestamp() {
    return timestamp_;
  }

  public static final int SIGNATURE_FIELD_NUMBER = 3;
  private com.google.protobuf.ByteString signature_ = com.google.protobuf.ByteString.EMPTY;
  /**
   * <code>bytes signature = 3;</code>
   * @return The signature.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getSignature() {
    return signature_;
  }

  public static final int SIGNER_FIELD_NUMBER = 4;
  private com.google.protobuf.ByteString signer_ = com.google.protobuf.ByteString.EMPTY;
  /**
   * <pre>
   * pubkey binary of the signing keypair
   * </pre>
   *
   * <code>bytes signer = 4;</code>
   * @return The signer.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getSigner() {
    return signer_;
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
    if (((bitField0_ & 0x00000001) != 0)) {
      output.writeMessage(1, getRoute());
    }
    if (timestamp_ != 0L) {
      output.writeUInt64(2, timestamp_);
    }
    if (!signature_.isEmpty()) {
      output.writeBytes(3, signature_);
    }
    if (!signer_.isEmpty()) {
      output.writeBytes(4, signer_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (((bitField0_ & 0x00000001) != 0)) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getRoute());
    }
    if (timestamp_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt64Size(2, timestamp_);
    }
    if (!signature_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(3, signature_);
    }
    if (!signer_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(4, signer_);
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
    if (!(obj instanceof com.helium.grpc.route_update_req_v1)) {
      return super.equals(obj);
    }
    com.helium.grpc.route_update_req_v1 other = (com.helium.grpc.route_update_req_v1) obj;

    if (hasRoute() != other.hasRoute()) return false;
    if (hasRoute()) {
      if (!getRoute()
          .equals(other.getRoute())) return false;
    }
    if (getTimestamp()
        != other.getTimestamp()) return false;
    if (!getSignature()
        .equals(other.getSignature())) return false;
    if (!getSigner()
        .equals(other.getSigner())) return false;
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
    if (hasRoute()) {
      hash = (37 * hash) + ROUTE_FIELD_NUMBER;
      hash = (53 * hash) + getRoute().hashCode();
    }
    hash = (37 * hash) + TIMESTAMP_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getTimestamp());
    hash = (37 * hash) + SIGNATURE_FIELD_NUMBER;
    hash = (53 * hash) + getSignature().hashCode();
    hash = (37 * hash) + SIGNER_FIELD_NUMBER;
    hash = (53 * hash) + getSigner().hashCode();
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.helium.grpc.route_update_req_v1 parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.helium.grpc.route_update_req_v1 parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.helium.grpc.route_update_req_v1 parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.helium.grpc.route_update_req_v1 parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.helium.grpc.route_update_req_v1 parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.helium.grpc.route_update_req_v1 parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.helium.grpc.route_update_req_v1 parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.helium.grpc.route_update_req_v1 parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static com.helium.grpc.route_update_req_v1 parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static com.helium.grpc.route_update_req_v1 parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.helium.grpc.route_update_req_v1 parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.helium.grpc.route_update_req_v1 parseFrom(
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
  public static Builder newBuilder(com.helium.grpc.route_update_req_v1 prototype) {
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
   * Protobuf type {@code helium.iot_config.route_update_req_v1}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:helium.iot_config.route_update_req_v1)
      com.helium.grpc.route_update_req_v1OrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.helium.grpc.IotConfig.internal_static_helium_iot_config_route_update_req_v1_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.helium.grpc.IotConfig.internal_static_helium_iot_config_route_update_req_v1_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.helium.grpc.route_update_req_v1.class, com.helium.grpc.route_update_req_v1.Builder.class);
    }

    // Construct using com.helium.grpc.route_update_req_v1.newBuilder()
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
        getRouteFieldBuilder();
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      route_ = null;
      if (routeBuilder_ != null) {
        routeBuilder_.dispose();
        routeBuilder_ = null;
      }
      timestamp_ = 0L;
      signature_ = com.google.protobuf.ByteString.EMPTY;
      signer_ = com.google.protobuf.ByteString.EMPTY;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.helium.grpc.IotConfig.internal_static_helium_iot_config_route_update_req_v1_descriptor;
    }

    @java.lang.Override
    public com.helium.grpc.route_update_req_v1 getDefaultInstanceForType() {
      return com.helium.grpc.route_update_req_v1.getDefaultInstance();
    }

    @java.lang.Override
    public com.helium.grpc.route_update_req_v1 build() {
      com.helium.grpc.route_update_req_v1 result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.helium.grpc.route_update_req_v1 buildPartial() {
      com.helium.grpc.route_update_req_v1 result = new com.helium.grpc.route_update_req_v1(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.helium.grpc.route_update_req_v1 result) {
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.route_ = routeBuilder_ == null
            ? route_
            : routeBuilder_.build();
        to_bitField0_ |= 0x00000001;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.timestamp_ = timestamp_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.signature_ = signature_;
      }
      if (((from_bitField0_ & 0x00000008) != 0)) {
        result.signer_ = signer_;
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
      if (other instanceof com.helium.grpc.route_update_req_v1) {
        return mergeFrom((com.helium.grpc.route_update_req_v1)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.helium.grpc.route_update_req_v1 other) {
      if (other == com.helium.grpc.route_update_req_v1.getDefaultInstance()) return this;
      if (other.hasRoute()) {
        mergeRoute(other.getRoute());
      }
      if (other.getTimestamp() != 0L) {
        setTimestamp(other.getTimestamp());
      }
      if (other.getSignature() != com.google.protobuf.ByteString.EMPTY) {
        setSignature(other.getSignature());
      }
      if (other.getSigner() != com.google.protobuf.ByteString.EMPTY) {
        setSigner(other.getSigner());
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
              input.readMessage(
                  getRouteFieldBuilder().getBuilder(),
                  extensionRegistry);
              bitField0_ |= 0x00000001;
              break;
            } // case 10
            case 16: {
              timestamp_ = input.readUInt64();
              bitField0_ |= 0x00000002;
              break;
            } // case 16
            case 26: {
              signature_ = input.readBytes();
              bitField0_ |= 0x00000004;
              break;
            } // case 26
            case 34: {
              signer_ = input.readBytes();
              bitField0_ |= 0x00000008;
              break;
            } // case 34
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

    private com.helium.grpc.route_v1 route_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.helium.grpc.route_v1, com.helium.grpc.route_v1.Builder, com.helium.grpc.route_v1OrBuilder> routeBuilder_;
    /**
     * <code>.helium.iot_config.route_v1 route = 1;</code>
     * @return Whether the route field is set.
     */
    public boolean hasRoute() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     * <code>.helium.iot_config.route_v1 route = 1;</code>
     * @return The route.
     */
    public com.helium.grpc.route_v1 getRoute() {
      if (routeBuilder_ == null) {
        return route_ == null ? com.helium.grpc.route_v1.getDefaultInstance() : route_;
      } else {
        return routeBuilder_.getMessage();
      }
    }
    /**
     * <code>.helium.iot_config.route_v1 route = 1;</code>
     */
    public Builder setRoute(com.helium.grpc.route_v1 value) {
      if (routeBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        route_ = value;
      } else {
        routeBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>.helium.iot_config.route_v1 route = 1;</code>
     */
    public Builder setRoute(
        com.helium.grpc.route_v1.Builder builderForValue) {
      if (routeBuilder_ == null) {
        route_ = builderForValue.build();
      } else {
        routeBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>.helium.iot_config.route_v1 route = 1;</code>
     */
    public Builder mergeRoute(com.helium.grpc.route_v1 value) {
      if (routeBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0) &&
          route_ != null &&
          route_ != com.helium.grpc.route_v1.getDefaultInstance()) {
          getRouteBuilder().mergeFrom(value);
        } else {
          route_ = value;
        }
      } else {
        routeBuilder_.mergeFrom(value);
      }
      if (route_ != null) {
        bitField0_ |= 0x00000001;
        onChanged();
      }
      return this;
    }
    /**
     * <code>.helium.iot_config.route_v1 route = 1;</code>
     */
    public Builder clearRoute() {
      bitField0_ = (bitField0_ & ~0x00000001);
      route_ = null;
      if (routeBuilder_ != null) {
        routeBuilder_.dispose();
        routeBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <code>.helium.iot_config.route_v1 route = 1;</code>
     */
    public com.helium.grpc.route_v1.Builder getRouteBuilder() {
      bitField0_ |= 0x00000001;
      onChanged();
      return getRouteFieldBuilder().getBuilder();
    }
    /**
     * <code>.helium.iot_config.route_v1 route = 1;</code>
     */
    public com.helium.grpc.route_v1OrBuilder getRouteOrBuilder() {
      if (routeBuilder_ != null) {
        return routeBuilder_.getMessageOrBuilder();
      } else {
        return route_ == null ?
            com.helium.grpc.route_v1.getDefaultInstance() : route_;
      }
    }
    /**
     * <code>.helium.iot_config.route_v1 route = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.helium.grpc.route_v1, com.helium.grpc.route_v1.Builder, com.helium.grpc.route_v1OrBuilder> 
        getRouteFieldBuilder() {
      if (routeBuilder_ == null) {
        routeBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.helium.grpc.route_v1, com.helium.grpc.route_v1.Builder, com.helium.grpc.route_v1OrBuilder>(
                getRoute(),
                getParentForChildren(),
                isClean());
        route_ = null;
      }
      return routeBuilder_;
    }

    private long timestamp_ ;
    /**
     * <pre>
     * in milliseconds since unix epoch
     * </pre>
     *
     * <code>uint64 timestamp = 2;</code>
     * @return The timestamp.
     */
    @java.lang.Override
    public long getTimestamp() {
      return timestamp_;
    }
    /**
     * <pre>
     * in milliseconds since unix epoch
     * </pre>
     *
     * <code>uint64 timestamp = 2;</code>
     * @param value The timestamp to set.
     * @return This builder for chaining.
     */
    public Builder setTimestamp(long value) {

      timestamp_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * in milliseconds since unix epoch
     * </pre>
     *
     * <code>uint64 timestamp = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearTimestamp() {
      bitField0_ = (bitField0_ & ~0x00000002);
      timestamp_ = 0L;
      onChanged();
      return this;
    }

    private com.google.protobuf.ByteString signature_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <code>bytes signature = 3;</code>
     * @return The signature.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString getSignature() {
      return signature_;
    }
    /**
     * <code>bytes signature = 3;</code>
     * @param value The signature to set.
     * @return This builder for chaining.
     */
    public Builder setSignature(com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      signature_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>bytes signature = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearSignature() {
      bitField0_ = (bitField0_ & ~0x00000004);
      signature_ = getDefaultInstance().getSignature();
      onChanged();
      return this;
    }

    private com.google.protobuf.ByteString signer_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <pre>
     * pubkey binary of the signing keypair
     * </pre>
     *
     * <code>bytes signer = 4;</code>
     * @return The signer.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString getSigner() {
      return signer_;
    }
    /**
     * <pre>
     * pubkey binary of the signing keypair
     * </pre>
     *
     * <code>bytes signer = 4;</code>
     * @param value The signer to set.
     * @return This builder for chaining.
     */
    public Builder setSigner(com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      signer_ = value;
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * pubkey binary of the signing keypair
     * </pre>
     *
     * <code>bytes signer = 4;</code>
     * @return This builder for chaining.
     */
    public Builder clearSigner() {
      bitField0_ = (bitField0_ & ~0x00000008);
      signer_ = getDefaultInstance().getSigner();
      onChanged();
      return this;
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


    // @@protoc_insertion_point(builder_scope:helium.iot_config.route_update_req_v1)
  }

  // @@protoc_insertion_point(class_scope:helium.iot_config.route_update_req_v1)
  private static final com.helium.grpc.route_update_req_v1 DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.helium.grpc.route_update_req_v1();
  }

  public static com.helium.grpc.route_update_req_v1 getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<route_update_req_v1>
      PARSER = new com.google.protobuf.AbstractParser<route_update_req_v1>() {
    @java.lang.Override
    public route_update_req_v1 parsePartialFrom(
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

  public static com.google.protobuf.Parser<route_update_req_v1> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<route_update_req_v1> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.helium.grpc.route_update_req_v1 getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

