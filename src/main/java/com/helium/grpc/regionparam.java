// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: blockchain_region_param_v1.proto

// Protobuf Java Version: 3.25.1
package com.helium.grpc;

public final class regionparam {
  private regionparam() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_helium_blockchain_region_params_v1_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_helium_blockchain_region_params_v1_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_helium_tagged_spreading_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_helium_tagged_spreading_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_helium_blockchain_region_spreading_v1_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_helium_blockchain_region_spreading_v1_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_helium_blockchain_region_param_v1_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_helium_blockchain_region_param_v1_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n blockchain_region_param_v1.proto\022\006heli" +
      "um\"X\n\033blockchain_region_params_v1\0229\n\rreg" +
      "ion_params\030\001 \003(\0132\".helium.blockchain_reg" +
      "ion_param_v1\"^\n\020tagged_spreading\0221\n\020regi" +
      "on_spreading\030\001 \001(\0162\027.helium.RegionSpread" +
      "ing\022\027\n\017max_packet_size\030\002 \001(\r\"T\n\036blockcha" +
      "in_region_spreading_v1\0222\n\020tagged_spreadi" +
      "ng\030\001 \003(\0132\030.helium.tagged_spreading\"\227\001\n\032b" +
      "lockchain_region_param_v1\022\031\n\021channel_fre" +
      "quency\030\001 \001(\004\022\021\n\tbandwidth\030\002 \001(\r\022\020\n\010max_e" +
      "irp\030\003 \001(\r\0229\n\tspreading\030\004 \001(\0132&.helium.bl" +
      "ockchain_region_spreading_v1*Z\n\017RegionSp" +
      "reading\022\016\n\nSF_INVALID\020\000\022\007\n\003SF7\020\001\022\007\n\003SF8\020" +
      "\002\022\007\n\003SF9\020\003\022\010\n\004SF10\020\004\022\010\n\004SF11\020\005\022\010\n\004SF12\020\006" +
      "B \n\017com.helium.grpcB\013regionparamP\001b\006prot" +
      "o3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_helium_blockchain_region_params_v1_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_helium_blockchain_region_params_v1_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_helium_blockchain_region_params_v1_descriptor,
        new java.lang.String[] { "RegionParams", });
    internal_static_helium_tagged_spreading_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_helium_tagged_spreading_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_helium_tagged_spreading_descriptor,
        new java.lang.String[] { "RegionSpreading", "MaxPacketSize", });
    internal_static_helium_blockchain_region_spreading_v1_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_helium_blockchain_region_spreading_v1_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_helium_blockchain_region_spreading_v1_descriptor,
        new java.lang.String[] { "TaggedSpreading", });
    internal_static_helium_blockchain_region_param_v1_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_helium_blockchain_region_param_v1_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_helium_blockchain_region_param_v1_descriptor,
        new java.lang.String[] { "ChannelFrequency", "Bandwidth", "MaxEirp", "Spreading", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
