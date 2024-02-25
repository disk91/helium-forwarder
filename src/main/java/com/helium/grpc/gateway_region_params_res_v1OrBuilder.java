// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: service/iot_config.proto

// Protobuf Java Version: 3.25.1
package com.helium.grpc;

public interface gateway_region_params_res_v1OrBuilder extends
    // @@protoc_insertion_point(interface_extends:helium.iot_config.gateway_region_params_res_v1)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.helium.region region = 1;</code>
   * @return The enum numeric value on the wire for region.
   */
  int getRegionValue();
  /**
   * <code>.helium.region region = 1;</code>
   * @return The region.
   */
  com.helium.grpc.region getRegion();

  /**
   * <code>.helium.blockchain_region_params_v1 params = 2;</code>
   * @return Whether the params field is set.
   */
  boolean hasParams();
  /**
   * <code>.helium.blockchain_region_params_v1 params = 2;</code>
   * @return The params.
   */
  com.helium.grpc.blockchain_region_params_v1 getParams();
  /**
   * <code>.helium.blockchain_region_params_v1 params = 2;</code>
   */
  com.helium.grpc.blockchain_region_params_v1OrBuilder getParamsOrBuilder();

  /**
   * <code>uint64 gain = 3;</code>
   * @return The gain.
   */
  long getGain();

  /**
   * <pre>
   * Signature over the response by the config service
   * </pre>
   *
   * <code>bytes signature = 4;</code>
   * @return The signature.
   */
  com.google.protobuf.ByteString getSignature();

  /**
   * <pre>
   * in seconds since unix epoch
   * </pre>
   *
   * <code>uint64 timestamp = 5;</code>
   * @return The timestamp.
   */
  long getTimestamp();

  /**
   * <pre>
   * pubkey binary of the signing keypair
   * </pre>
   *
   * <code>bytes signer = 6;</code>
   * @return The signer.
   */
  com.google.protobuf.ByteString getSigner();
}
