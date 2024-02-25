// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: service/iot_config.proto

// Protobuf Java Version: 3.25.1
package com.helium.grpc;

/**
 * <pre>
 *&#47; Request a stream of all active gateways
 * </pre>
 *
 * Protobuf type {@code helium.iot_config.gateway_info_stream_req_v1}
 */
public final class gateway_info_stream_req_v1 extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:helium.iot_config.gateway_info_stream_req_v1)
    gateway_info_stream_req_v1OrBuilder {
private static final long serialVersionUID = 0L;
  // Use gateway_info_stream_req_v1.newBuilder() to construct.
  private gateway_info_stream_req_v1(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private gateway_info_stream_req_v1() {
    signature_ = com.google.protobuf.ByteString.EMPTY;
    signer_ = com.google.protobuf.ByteString.EMPTY;
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new gateway_info_stream_req_v1();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.helium.grpc.IotConfig.internal_static_helium_iot_config_gateway_info_stream_req_v1_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.helium.grpc.IotConfig.internal_static_helium_iot_config_gateway_info_stream_req_v1_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.helium.grpc.gateway_info_stream_req_v1.class, com.helium.grpc.gateway_info_stream_req_v1.Builder.class);
  }

  public static final int BATCH_SIZE_FIELD_NUMBER = 1;
  private int batchSize_ = 0;
  /**
   * <code>uint32 batch_size = 1;</code>
   * @return The batchSize.
   */
  @java.lang.Override
  public int getBatchSize() {
    return batchSize_;
  }

  public static final int SIGNATURE_FIELD_NUMBER = 2;
  private com.google.protobuf.ByteString signature_ = com.google.protobuf.ByteString.EMPTY;
  /**
   * <pre>
   *&#47; sig from a key known to the config service
   * </pre>
   *
   * <code>bytes signature = 2;</code>
   * @return The signature.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getSignature() {
    return signature_;
  }

  public static final int SIGNER_FIELD_NUMBER = 3;
  private com.google.protobuf.ByteString signer_ = com.google.protobuf.ByteString.EMPTY;
  /**
   * <pre>
   * pubkey binary of the signing keypair
   * </pre>
   *
   * <code>bytes signer = 3;</code>
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
    if (batchSize_ != 0) {
      output.writeUInt32(1, batchSize_);
    }
    if (!signature_.isEmpty()) {
      output.writeBytes(2, signature_);
    }
    if (!signer_.isEmpty()) {
      output.writeBytes(3, signer_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (batchSize_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt32Size(1, batchSize_);
    }
    if (!signature_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(2, signature_);
    }
    if (!signer_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(3, signer_);
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
    if (!(obj instanceof com.helium.grpc.gateway_info_stream_req_v1)) {
      return super.equals(obj);
    }
    com.helium.grpc.gateway_info_stream_req_v1 other = (com.helium.grpc.gateway_info_stream_req_v1) obj;

    if (getBatchSize()
        != other.getBatchSize()) return false;
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
    hash = (37 * hash) + BATCH_SIZE_FIELD_NUMBER;
    hash = (53 * hash) + getBatchSize();
    hash = (37 * hash) + SIGNATURE_FIELD_NUMBER;
    hash = (53 * hash) + getSignature().hashCode();
    hash = (37 * hash) + SIGNER_FIELD_NUMBER;
    hash = (53 * hash) + getSigner().hashCode();
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.helium.grpc.gateway_info_stream_req_v1 parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.helium.grpc.gateway_info_stream_req_v1 parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.helium.grpc.gateway_info_stream_req_v1 parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.helium.grpc.gateway_info_stream_req_v1 parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.helium.grpc.gateway_info_stream_req_v1 parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.helium.grpc.gateway_info_stream_req_v1 parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.helium.grpc.gateway_info_stream_req_v1 parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.helium.grpc.gateway_info_stream_req_v1 parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static com.helium.grpc.gateway_info_stream_req_v1 parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static com.helium.grpc.gateway_info_stream_req_v1 parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.helium.grpc.gateway_info_stream_req_v1 parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.helium.grpc.gateway_info_stream_req_v1 parseFrom(
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
  public static Builder newBuilder(com.helium.grpc.gateway_info_stream_req_v1 prototype) {
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
   * <pre>
   *&#47; Request a stream of all active gateways
   * </pre>
   *
   * Protobuf type {@code helium.iot_config.gateway_info_stream_req_v1}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:helium.iot_config.gateway_info_stream_req_v1)
      com.helium.grpc.gateway_info_stream_req_v1OrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.helium.grpc.IotConfig.internal_static_helium_iot_config_gateway_info_stream_req_v1_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.helium.grpc.IotConfig.internal_static_helium_iot_config_gateway_info_stream_req_v1_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.helium.grpc.gateway_info_stream_req_v1.class, com.helium.grpc.gateway_info_stream_req_v1.Builder.class);
    }

    // Construct using com.helium.grpc.gateway_info_stream_req_v1.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      batchSize_ = 0;
      signature_ = com.google.protobuf.ByteString.EMPTY;
      signer_ = com.google.protobuf.ByteString.EMPTY;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.helium.grpc.IotConfig.internal_static_helium_iot_config_gateway_info_stream_req_v1_descriptor;
    }

    @java.lang.Override
    public com.helium.grpc.gateway_info_stream_req_v1 getDefaultInstanceForType() {
      return com.helium.grpc.gateway_info_stream_req_v1.getDefaultInstance();
    }

    @java.lang.Override
    public com.helium.grpc.gateway_info_stream_req_v1 build() {
      com.helium.grpc.gateway_info_stream_req_v1 result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.helium.grpc.gateway_info_stream_req_v1 buildPartial() {
      com.helium.grpc.gateway_info_stream_req_v1 result = new com.helium.grpc.gateway_info_stream_req_v1(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.helium.grpc.gateway_info_stream_req_v1 result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.batchSize_ = batchSize_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.signature_ = signature_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.signer_ = signer_;
      }
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
      if (other instanceof com.helium.grpc.gateway_info_stream_req_v1) {
        return mergeFrom((com.helium.grpc.gateway_info_stream_req_v1)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.helium.grpc.gateway_info_stream_req_v1 other) {
      if (other == com.helium.grpc.gateway_info_stream_req_v1.getDefaultInstance()) return this;
      if (other.getBatchSize() != 0) {
        setBatchSize(other.getBatchSize());
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
            case 8: {
              batchSize_ = input.readUInt32();
              bitField0_ |= 0x00000001;
              break;
            } // case 8
            case 18: {
              signature_ = input.readBytes();
              bitField0_ |= 0x00000002;
              break;
            } // case 18
            case 26: {
              signer_ = input.readBytes();
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

    private int batchSize_ ;
    /**
     * <code>uint32 batch_size = 1;</code>
     * @return The batchSize.
     */
    @java.lang.Override
    public int getBatchSize() {
      return batchSize_;
    }
    /**
     * <code>uint32 batch_size = 1;</code>
     * @param value The batchSize to set.
     * @return This builder for chaining.
     */
    public Builder setBatchSize(int value) {

      batchSize_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>uint32 batch_size = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearBatchSize() {
      bitField0_ = (bitField0_ & ~0x00000001);
      batchSize_ = 0;
      onChanged();
      return this;
    }

    private com.google.protobuf.ByteString signature_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <pre>
     *&#47; sig from a key known to the config service
     * </pre>
     *
     * <code>bytes signature = 2;</code>
     * @return The signature.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString getSignature() {
      return signature_;
    }
    /**
     * <pre>
     *&#47; sig from a key known to the config service
     * </pre>
     *
     * <code>bytes signature = 2;</code>
     * @param value The signature to set.
     * @return This builder for chaining.
     */
    public Builder setSignature(com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      signature_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <pre>
     *&#47; sig from a key known to the config service
     * </pre>
     *
     * <code>bytes signature = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearSignature() {
      bitField0_ = (bitField0_ & ~0x00000002);
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
     * <code>bytes signer = 3;</code>
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
     * <code>bytes signer = 3;</code>
     * @param value The signer to set.
     * @return This builder for chaining.
     */
    public Builder setSigner(com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      signer_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * pubkey binary of the signing keypair
     * </pre>
     *
     * <code>bytes signer = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearSigner() {
      bitField0_ = (bitField0_ & ~0x00000004);
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


    // @@protoc_insertion_point(builder_scope:helium.iot_config.gateway_info_stream_req_v1)
  }

  // @@protoc_insertion_point(class_scope:helium.iot_config.gateway_info_stream_req_v1)
  private static final com.helium.grpc.gateway_info_stream_req_v1 DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.helium.grpc.gateway_info_stream_req_v1();
  }

  public static com.helium.grpc.gateway_info_stream_req_v1 getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<gateway_info_stream_req_v1>
      PARSER = new com.google.protobuf.AbstractParser<gateway_info_stream_req_v1>() {
    @java.lang.Override
    public gateway_info_stream_req_v1 parsePartialFrom(
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

  public static com.google.protobuf.Parser<gateway_info_stream_req_v1> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<gateway_info_stream_req_v1> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.helium.grpc.gateway_info_stream_req_v1 getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

