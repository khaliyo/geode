/*=========================================================================
 * Copyright (c) 2002-2014 Pivotal Software, Inc. All Rights Reserved.
 * This product is protected by U.S. and international copyright
 * and intellectual property laws. Pivotal products are covered by
 * more patents listed at http://www.pivotal.io/patents.
 *=========================================================================
 */
package com.gemstone.gemfire.internal.cache;

import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.distributed.internal.membership.InternalDistributedMember;
import com.gemstone.gemfire.internal.cache.versions.VersionSource;
import com.gemstone.gemfire.internal.cache.versions.VersionStamp;
import com.gemstone.gemfire.internal.cache.versions.VersionTag;

/**
 * @author bruce
 *
 */
public abstract class VersionedThinLRURegionEntry extends VMThinLRURegionEntry
  implements VersionStamp {
  
  protected VersionedThinLRURegionEntry(RegionEntryContext context, Object value) {
    super(context, value);
  }

  // Do not add any instance fields to this class.
  // Instead add them to the VERSIONED section of LeafRegionEntry.cpp.
}
