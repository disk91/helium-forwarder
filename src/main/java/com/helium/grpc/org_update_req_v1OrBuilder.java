// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: service/iot_config.proto

// Protobuf Java Version: 3.25.1
package com.helium.grpc;

public interface org_update_req_v1OrBuilder extends
    // @@protoc_insertion_point(interface_extends:helium.iot_config.org_update_req_v1)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>uint64 oui = 1;</code>
   * @return The oui.
   */
  long getOui();

  /**
   * <code>repeated .helium.iot_config.org_update_req_v1.update_v1 updates = 2;</code>
   */
  java.util.List<com.helium.grpc.org_update_req_v1.update_v1> 
      getUpdatesList();
  /**
   * <code>repeated .helium.iot_config.org_update_req_v1.update_v1 updates = 2;</code>
   */
  com.helium.grpc.org_update_req_v1.update_v1 getUpdates(int index);
  /**
   * <code>repeated .helium.iot_config.org_update_req_v1.update_v1 updates = 2;</code>
   */
  int getUpdatesCount();
  /**
   * <code>repeated .helium.iot_config.org_update_req_v1.update_v1 updates = 2;</code>
   */
  java.util.List<? extends com.helium.grpc.org_update_req_v1.update_v1OrBuilder> 
      getUpdatesOrBuilderList();
  /**
   * <code>repeated .helium.iot_config.org_update_req_v1.update_v1 updates = 2;</code>
   */
  com.helium.grpc.org_update_req_v1.update_v1OrBuilder getUpdatesOrBuilder(
      int index);

  /**
   * <code>uint64 timestamp = 3;</code>
   * @return The timestamp.
   */
  long getTimestamp();

  /**
   * <code>bytes signer = 4;</code>
   * @return The signer.
   */
  com.google.protobuf.ByteString getSigner();

  /**
   * <code>bytes signature = 5;</code>
   * @return The signature.
   */
  com.google.protobuf.ByteString getSignature();
}
