// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: service/iot_config.proto

package com.helium.grpc;

/**
 * <pre>
 * Route definition
 * </pre>
 *
 * Protobuf type {@code helium.iot_config.route_v1}
 */
public final class route_v1 extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:helium.iot_config.route_v1)
    route_v1OrBuilder {
private static final long serialVersionUID = 0L;
  // Use route_v1.newBuilder() to construct.
  private route_v1(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private route_v1() {
    id_ = "";
  }

  @Override
  @SuppressWarnings({"unused"})
  protected Object newInstance(
      UnusedPrivateParameter unused) {
    return new route_v1();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return IotConfig.internal_static_helium_iot_config_route_v1_descriptor;
  }

  @Override
  protected FieldAccessorTable
      internalGetFieldAccessorTable() {
    return IotConfig.internal_static_helium_iot_config_route_v1_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            route_v1.class, Builder.class);
  }

  public static final int ID_FIELD_NUMBER = 1;
  @SuppressWarnings("serial")
  private volatile Object id_ = "";
  /**
   * <pre>
   * UUID
   * </pre>
   *
   * <code>string id = 1;</code>
   * @return The id.
   */
  @Override
  public String getId() {
    Object ref = id_;
    if (ref instanceof String) {
      return (String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      String s = bs.toStringUtf8();
      id_ = s;
      return s;
    }
  }
  /**
   * <pre>
   * UUID
   * </pre>
   *
   * <code>string id = 1;</code>
   * @return The bytes for id.
   */
  @Override
  public com.google.protobuf.ByteString
      getIdBytes() {
    Object ref = id_;
    if (ref instanceof String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (String) ref);
      id_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int NET_ID_FIELD_NUMBER = 2;
  private int netId_ = 0;
  /**
   * <pre>
   * LoraWan Network ID
   * </pre>
   *
   * <code>uint32 net_id = 2;</code>
   * @return The netId.
   */
  @Override
  public int getNetId() {
    return netId_;
  }

  public static final int OUI_FIELD_NUMBER = 3;
  private long oui_ = 0L;
  /**
   * <pre>
   * Organization Unique ID
   * </pre>
   *
   * <code>uint64 oui = 3;</code>
   * @return The oui.
   */
  @Override
  public long getOui() {
    return oui_;
  }

  public static final int SERVER_FIELD_NUMBER = 4;
  private server_v1 server_;
  /**
   * <code>.helium.iot_config.server_v1 server = 4;</code>
   * @return Whether the server field is set.
   */
  @Override
  public boolean hasServer() {
    return server_ != null;
  }
  /**
   * <code>.helium.iot_config.server_v1 server = 4;</code>
   * @return The server.
   */
  @Override
  public server_v1 getServer() {
    return server_ == null ? server_v1.getDefaultInstance() : server_;
  }
  /**
   * <code>.helium.iot_config.server_v1 server = 4;</code>
   */
  @Override
  public server_v1OrBuilder getServerOrBuilder() {
    return server_ == null ? server_v1.getDefaultInstance() : server_;
  }

  public static final int MAX_COPIES_FIELD_NUMBER = 5;
  private int maxCopies_ = 0;
  /**
   * <pre>
   * Number of packet copies bought by this route
   * </pre>
   *
   * <code>uint32 max_copies = 5;</code>
   * @return The maxCopies.
   */
  @Override
  public int getMaxCopies() {
    return maxCopies_;
  }

  public static final int ACTIVE_FIELD_NUMBER = 6;
  private boolean active_ = false;
  /**
   * <code>bool active = 6;</code>
   * @return The active.
   */
  @Override
  public boolean getActive() {
    return active_;
  }

  public static final int LOCKED_FIELD_NUMBER = 7;
  private boolean locked_ = false;
  /**
   * <pre>
   * Is route locked because of no payment
   * </pre>
   *
   * <code>bool locked = 7;</code>
   * @return The locked.
   */
  @Override
  public boolean getLocked() {
    return locked_;
  }

  public static final int IGNORE_EMPTY_SKF_FIELD_NUMBER = 8;
  private boolean ignoreEmptySkf_ = false;
  /**
   * <pre>
   * If true, routes are blocked whose devaddrs have empty session key filters
   * </pre>
   *
   * <code>bool ignore_empty_skf = 8;</code>
   * @return The ignoreEmptySkf.
   */
  @Override
  public boolean getIgnoreEmptySkf() {
    return ignoreEmptySkf_;
  }

  private byte memoizedIsInitialized = -1;
  @Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(id_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, id_);
    }
    if (netId_ != 0) {
      output.writeUInt32(2, netId_);
    }
    if (oui_ != 0L) {
      output.writeUInt64(3, oui_);
    }
    if (server_ != null) {
      output.writeMessage(4, getServer());
    }
    if (maxCopies_ != 0) {
      output.writeUInt32(5, maxCopies_);
    }
    if (active_ != false) {
      output.writeBool(6, active_);
    }
    if (locked_ != false) {
      output.writeBool(7, locked_);
    }
    if (ignoreEmptySkf_ != false) {
      output.writeBool(8, ignoreEmptySkf_);
    }
    getUnknownFields().writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(id_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, id_);
    }
    if (netId_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt32Size(2, netId_);
    }
    if (oui_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt64Size(3, oui_);
    }
    if (server_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(4, getServer());
    }
    if (maxCopies_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeUInt32Size(5, maxCopies_);
    }
    if (active_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(6, active_);
    }
    if (locked_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(7, locked_);
    }
    if (ignoreEmptySkf_ != false) {
      size += com.google.protobuf.CodedOutputStream
        .computeBoolSize(8, ignoreEmptySkf_);
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof route_v1)) {
      return super.equals(obj);
    }
    route_v1 other = (route_v1) obj;

    if (!getId()
        .equals(other.getId())) return false;
    if (getNetId()
        != other.getNetId()) return false;
    if (getOui()
        != other.getOui()) return false;
    if (hasServer() != other.hasServer()) return false;
    if (hasServer()) {
      if (!getServer()
          .equals(other.getServer())) return false;
    }
    if (getMaxCopies()
        != other.getMaxCopies()) return false;
    if (getActive()
        != other.getActive()) return false;
    if (getLocked()
        != other.getLocked()) return false;
    if (getIgnoreEmptySkf()
        != other.getIgnoreEmptySkf()) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + ID_FIELD_NUMBER;
    hash = (53 * hash) + getId().hashCode();
    hash = (37 * hash) + NET_ID_FIELD_NUMBER;
    hash = (53 * hash) + getNetId();
    hash = (37 * hash) + OUI_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getOui());
    if (hasServer()) {
      hash = (37 * hash) + SERVER_FIELD_NUMBER;
      hash = (53 * hash) + getServer().hashCode();
    }
    hash = (37 * hash) + MAX_COPIES_FIELD_NUMBER;
    hash = (53 * hash) + getMaxCopies();
    hash = (37 * hash) + ACTIVE_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getActive());
    hash = (37 * hash) + LOCKED_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getLocked());
    hash = (37 * hash) + IGNORE_EMPTY_SKF_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(
        getIgnoreEmptySkf());
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static route_v1 parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static route_v1 parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static route_v1 parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static route_v1 parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static route_v1 parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static route_v1 parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static route_v1 parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static route_v1 parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static route_v1 parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static route_v1 parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static route_v1 parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static route_v1 parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(route_v1 prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @Override
  protected Builder newBuilderForType(
      BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   * Route definition
   * </pre>
   *
   * Protobuf type {@code helium.iot_config.route_v1}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:helium.iot_config.route_v1)
      route_v1OrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return IotConfig.internal_static_helium_iot_config_route_v1_descriptor;
    }

    @Override
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return IotConfig.internal_static_helium_iot_config_route_v1_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              route_v1.class, Builder.class);
    }

    // Construct using com.helium.grpc.route_v1.newBuilder()
    private Builder() {

    }

    private Builder(
        BuilderParent parent) {
      super(parent);

    }
    @Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      id_ = "";
      netId_ = 0;
      oui_ = 0L;
      server_ = null;
      if (serverBuilder_ != null) {
        serverBuilder_.dispose();
        serverBuilder_ = null;
      }
      maxCopies_ = 0;
      active_ = false;
      locked_ = false;
      ignoreEmptySkf_ = false;
      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return IotConfig.internal_static_helium_iot_config_route_v1_descriptor;
    }

    @Override
    public route_v1 getDefaultInstanceForType() {
      return route_v1.getDefaultInstance();
    }

    @Override
    public route_v1 build() {
      route_v1 result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public route_v1 buildPartial() {
      route_v1 result = new route_v1(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(route_v1 result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.id_ = id_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.netId_ = netId_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.oui_ = oui_;
      }
      if (((from_bitField0_ & 0x00000008) != 0)) {
        result.server_ = serverBuilder_ == null
            ? server_
            : serverBuilder_.build();
      }
      if (((from_bitField0_ & 0x00000010) != 0)) {
        result.maxCopies_ = maxCopies_;
      }
      if (((from_bitField0_ & 0x00000020) != 0)) {
        result.active_ = active_;
      }
      if (((from_bitField0_ & 0x00000040) != 0)) {
        result.locked_ = locked_;
      }
      if (((from_bitField0_ & 0x00000080) != 0)) {
        result.ignoreEmptySkf_ = ignoreEmptySkf_;
      }
    }

    @Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof route_v1) {
        return mergeFrom((route_v1)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(route_v1 other) {
      if (other == route_v1.getDefaultInstance()) return this;
      if (!other.getId().isEmpty()) {
        id_ = other.id_;
        bitField0_ |= 0x00000001;
        onChanged();
      }
      if (other.getNetId() != 0) {
        setNetId(other.getNetId());
      }
      if (other.getOui() != 0L) {
        setOui(other.getOui());
      }
      if (other.hasServer()) {
        mergeServer(other.getServer());
      }
      if (other.getMaxCopies() != 0) {
        setMaxCopies(other.getMaxCopies());
      }
      if (other.getActive() != false) {
        setActive(other.getActive());
      }
      if (other.getLocked() != false) {
        setLocked(other.getLocked());
      }
      if (other.getIgnoreEmptySkf() != false) {
        setIgnoreEmptySkf(other.getIgnoreEmptySkf());
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @Override
    public final boolean isInitialized() {
      return true;
    }

    @Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new NullPointerException();
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
              id_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000001;
              break;
            } // case 10
            case 16: {
              netId_ = input.readUInt32();
              bitField0_ |= 0x00000002;
              break;
            } // case 16
            case 24: {
              oui_ = input.readUInt64();
              bitField0_ |= 0x00000004;
              break;
            } // case 24
            case 34: {
              input.readMessage(
                  getServerFieldBuilder().getBuilder(),
                  extensionRegistry);
              bitField0_ |= 0x00000008;
              break;
            } // case 34
            case 40: {
              maxCopies_ = input.readUInt32();
              bitField0_ |= 0x00000010;
              break;
            } // case 40
            case 48: {
              active_ = input.readBool();
              bitField0_ |= 0x00000020;
              break;
            } // case 48
            case 56: {
              locked_ = input.readBool();
              bitField0_ |= 0x00000040;
              break;
            } // case 56
            case 64: {
              ignoreEmptySkf_ = input.readBool();
              bitField0_ |= 0x00000080;
              break;
            } // case 64
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

    private Object id_ = "";
    /**
     * <pre>
     * UUID
     * </pre>
     *
     * <code>string id = 1;</code>
     * @return The id.
     */
    public String getId() {
      Object ref = id_;
      if (!(ref instanceof String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        id_ = s;
        return s;
      } else {
        return (String) ref;
      }
    }
    /**
     * <pre>
     * UUID
     * </pre>
     *
     * <code>string id = 1;</code>
     * @return The bytes for id.
     */
    public com.google.protobuf.ByteString
        getIdBytes() {
      Object ref = id_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (String) ref);
        id_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <pre>
     * UUID
     * </pre>
     *
     * <code>string id = 1;</code>
     * @param value The id to set.
     * @return This builder for chaining.
     */
    public Builder setId(
        String value) {
      if (value == null) { throw new NullPointerException(); }
      id_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * UUID
     * </pre>
     *
     * <code>string id = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearId() {
      id_ = getDefaultInstance().getId();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }
    /**
     * <pre>
     * UUID
     * </pre>
     *
     * <code>string id = 1;</code>
     * @param value The bytes for id to set.
     * @return This builder for chaining.
     */
    public Builder setIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      id_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }

    private int netId_ ;
    /**
     * <pre>
     * LoraWan Network ID
     * </pre>
     *
     * <code>uint32 net_id = 2;</code>
     * @return The netId.
     */
    @Override
    public int getNetId() {
      return netId_;
    }
    /**
     * <pre>
     * LoraWan Network ID
     * </pre>
     *
     * <code>uint32 net_id = 2;</code>
     * @param value The netId to set.
     * @return This builder for chaining.
     */
    public Builder setNetId(int value) {

      netId_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * LoraWan Network ID
     * </pre>
     *
     * <code>uint32 net_id = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearNetId() {
      bitField0_ = (bitField0_ & ~0x00000002);
      netId_ = 0;
      onChanged();
      return this;
    }

    private long oui_ ;
    /**
     * <pre>
     * Organization Unique ID
     * </pre>
     *
     * <code>uint64 oui = 3;</code>
     * @return The oui.
     */
    @Override
    public long getOui() {
      return oui_;
    }
    /**
     * <pre>
     * Organization Unique ID
     * </pre>
     *
     * <code>uint64 oui = 3;</code>
     * @param value The oui to set.
     * @return This builder for chaining.
     */
    public Builder setOui(long value) {

      oui_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * Organization Unique ID
     * </pre>
     *
     * <code>uint64 oui = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearOui() {
      bitField0_ = (bitField0_ & ~0x00000004);
      oui_ = 0L;
      onChanged();
      return this;
    }

    private server_v1 server_;
    private com.google.protobuf.SingleFieldBuilderV3<
        server_v1, server_v1.Builder, server_v1OrBuilder> serverBuilder_;
    /**
     * <code>.helium.iot_config.server_v1 server = 4;</code>
     * @return Whether the server field is set.
     */
    public boolean hasServer() {
      return ((bitField0_ & 0x00000008) != 0);
    }
    /**
     * <code>.helium.iot_config.server_v1 server = 4;</code>
     * @return The server.
     */
    public server_v1 getServer() {
      if (serverBuilder_ == null) {
        return server_ == null ? server_v1.getDefaultInstance() : server_;
      } else {
        return serverBuilder_.getMessage();
      }
    }
    /**
     * <code>.helium.iot_config.server_v1 server = 4;</code>
     */
    public Builder setServer(server_v1 value) {
      if (serverBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        server_ = value;
      } else {
        serverBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>.helium.iot_config.server_v1 server = 4;</code>
     */
    public Builder setServer(
        server_v1.Builder builderForValue) {
      if (serverBuilder_ == null) {
        server_ = builderForValue.build();
      } else {
        serverBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>.helium.iot_config.server_v1 server = 4;</code>
     */
    public Builder mergeServer(server_v1 value) {
      if (serverBuilder_ == null) {
        if (((bitField0_ & 0x00000008) != 0) &&
          server_ != null &&
          server_ != server_v1.getDefaultInstance()) {
          getServerBuilder().mergeFrom(value);
        } else {
          server_ = value;
        }
      } else {
        serverBuilder_.mergeFrom(value);
      }
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>.helium.iot_config.server_v1 server = 4;</code>
     */
    public Builder clearServer() {
      bitField0_ = (bitField0_ & ~0x00000008);
      server_ = null;
      if (serverBuilder_ != null) {
        serverBuilder_.dispose();
        serverBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     * <code>.helium.iot_config.server_v1 server = 4;</code>
     */
    public server_v1.Builder getServerBuilder() {
      bitField0_ |= 0x00000008;
      onChanged();
      return getServerFieldBuilder().getBuilder();
    }
    /**
     * <code>.helium.iot_config.server_v1 server = 4;</code>
     */
    public server_v1OrBuilder getServerOrBuilder() {
      if (serverBuilder_ != null) {
        return serverBuilder_.getMessageOrBuilder();
      } else {
        return server_ == null ?
            server_v1.getDefaultInstance() : server_;
      }
    }
    /**
     * <code>.helium.iot_config.server_v1 server = 4;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        server_v1, server_v1.Builder, server_v1OrBuilder>
        getServerFieldBuilder() {
      if (serverBuilder_ == null) {
        serverBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            server_v1, server_v1.Builder, server_v1OrBuilder>(
                getServer(),
                getParentForChildren(),
                isClean());
        server_ = null;
      }
      return serverBuilder_;
    }

    private int maxCopies_ ;
    /**
     * <pre>
     * Number of packet copies bought by this route
     * </pre>
     *
     * <code>uint32 max_copies = 5;</code>
     * @return The maxCopies.
     */
    @Override
    public int getMaxCopies() {
      return maxCopies_;
    }
    /**
     * <pre>
     * Number of packet copies bought by this route
     * </pre>
     *
     * <code>uint32 max_copies = 5;</code>
     * @param value The maxCopies to set.
     * @return This builder for chaining.
     */
    public Builder setMaxCopies(int value) {

      maxCopies_ = value;
      bitField0_ |= 0x00000010;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * Number of packet copies bought by this route
     * </pre>
     *
     * <code>uint32 max_copies = 5;</code>
     * @return This builder for chaining.
     */
    public Builder clearMaxCopies() {
      bitField0_ = (bitField0_ & ~0x00000010);
      maxCopies_ = 0;
      onChanged();
      return this;
    }

    private boolean active_ ;
    /**
     * <code>bool active = 6;</code>
     * @return The active.
     */
    @Override
    public boolean getActive() {
      return active_;
    }
    /**
     * <code>bool active = 6;</code>
     * @param value The active to set.
     * @return This builder for chaining.
     */
    public Builder setActive(boolean value) {

      active_ = value;
      bitField0_ |= 0x00000020;
      onChanged();
      return this;
    }
    /**
     * <code>bool active = 6;</code>
     * @return This builder for chaining.
     */
    public Builder clearActive() {
      bitField0_ = (bitField0_ & ~0x00000020);
      active_ = false;
      onChanged();
      return this;
    }

    private boolean locked_ ;
    /**
     * <pre>
     * Is route locked because of no payment
     * </pre>
     *
     * <code>bool locked = 7;</code>
     * @return The locked.
     */
    @Override
    public boolean getLocked() {
      return locked_;
    }
    /**
     * <pre>
     * Is route locked because of no payment
     * </pre>
     *
     * <code>bool locked = 7;</code>
     * @param value The locked to set.
     * @return This builder for chaining.
     */
    public Builder setLocked(boolean value) {

      locked_ = value;
      bitField0_ |= 0x00000040;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * Is route locked because of no payment
     * </pre>
     *
     * <code>bool locked = 7;</code>
     * @return This builder for chaining.
     */
    public Builder clearLocked() {
      bitField0_ = (bitField0_ & ~0x00000040);
      locked_ = false;
      onChanged();
      return this;
    }

    private boolean ignoreEmptySkf_ ;
    /**
     * <pre>
     * If true, routes are blocked whose devaddrs have empty session key filters
     * </pre>
     *
     * <code>bool ignore_empty_skf = 8;</code>
     * @return The ignoreEmptySkf.
     */
    @Override
    public boolean getIgnoreEmptySkf() {
      return ignoreEmptySkf_;
    }
    /**
     * <pre>
     * If true, routes are blocked whose devaddrs have empty session key filters
     * </pre>
     *
     * <code>bool ignore_empty_skf = 8;</code>
     * @param value The ignoreEmptySkf to set.
     * @return This builder for chaining.
     */
    public Builder setIgnoreEmptySkf(boolean value) {

      ignoreEmptySkf_ = value;
      bitField0_ |= 0x00000080;
      onChanged();
      return this;
    }
    /**
     * <pre>
     * If true, routes are blocked whose devaddrs have empty session key filters
     * </pre>
     *
     * <code>bool ignore_empty_skf = 8;</code>
     * @return This builder for chaining.
     */
    public Builder clearIgnoreEmptySkf() {
      bitField0_ = (bitField0_ & ~0x00000080);
      ignoreEmptySkf_ = false;
      onChanged();
      return this;
    }
    @Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:helium.iot_config.route_v1)
  }

  // @@protoc_insertion_point(class_scope:helium.iot_config.route_v1)
  private static final route_v1 DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new route_v1();
  }

  public static route_v1 getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<route_v1>
      PARSER = new com.google.protobuf.AbstractParser<route_v1>() {
    @Override
    public route_v1 parsePartialFrom(
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

  public static com.google.protobuf.Parser<route_v1> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<route_v1> getParserForType() {
    return PARSER;
  }

  @Override
  public route_v1 getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}
