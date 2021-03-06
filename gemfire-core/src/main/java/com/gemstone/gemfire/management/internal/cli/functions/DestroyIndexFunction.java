/*=========================================================================
 * Copyright (c) 2010-2014 Pivotal Software, Inc. All Rights Reserved.
 * This product is protected by U.S. and international copyright
 * and intellectual property laws. Pivotal products are covered by
 * one or more patents listed at http://www.pivotal.io/patents.
 *=========================================================================
 */
package com.gemstone.gemfire.management.internal.cli.functions;

import java.util.List;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.CacheClosedException;
import com.gemstone.gemfire.cache.CacheFactory;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.FunctionAdapter;
import com.gemstone.gemfire.cache.execute.FunctionContext;
import com.gemstone.gemfire.cache.query.Index;
import com.gemstone.gemfire.cache.query.QueryService;
import com.gemstone.gemfire.internal.InternalEntity;
import com.gemstone.gemfire.internal.cache.xmlcache.CacheXml;
import com.gemstone.gemfire.management.internal.cli.domain.IndexInfo;
import com.gemstone.gemfire.management.internal.cli.i18n.CliStrings;
import com.gemstone.gemfire.management.internal.configuration.domain.XmlEntity;



public class DestroyIndexFunction extends FunctionAdapter implements
InternalEntity {

  private static final long serialVersionUID = 1L;

  @Override
  public void execute(FunctionContext context) {
    IndexInfo indexInfo = (IndexInfo)context.getArguments();
    String memberId = null;

    try {
      Cache cache = CacheFactory.getAnyInstance();
      memberId = cache.getDistributedSystem().getDistributedMember().getId();
      QueryService queryService = cache.getQueryService();
      String indexName = indexInfo.getIndexName();
      String regionPath = indexInfo.getRegionPath();
      
      XmlEntity xmlEntity = new XmlEntity(CacheXml.REGION, "name", regionPath, CacheXml.INDEX, "name", indexName); 
      
      if (regionPath != null && !regionPath.isEmpty()) {
        Region<?, ?> region = cache.getRegion(regionPath);
        
        if (region != null) {
          if (indexName == null || indexName.isEmpty()) {
            queryService.removeIndexes(region);
            context.getResultSender().lastResult(new CliFunctionResult(memberId, xmlEntity));
          } else {
            Index index = queryService.getIndex(region, indexName);
            
            if (index != null) {
              queryService.removeIndex(index);
              context.getResultSender().lastResult(new CliFunctionResult(memberId, xmlEntity));
            } else {
              context.getResultSender().lastResult(new CliFunctionResult(memberId, false, CliStrings.format(CliStrings.DESTROY_INDEX__INDEX__NOT__FOUND, indexName)));
            }
          }
        } else {
          context.getResultSender().lastResult(new CliFunctionResult(memberId, false, CliStrings.format(CliStrings.DESTROY_INDEX__REGION__NOT__FOUND, regionPath)));
        }

      } else {
        if (indexName == null || indexName.isEmpty()) {
          queryService.removeIndexes();
          context.getResultSender().lastResult(new CliFunctionResult(memberId, xmlEntity));
        } else {
          if (removeIndexByName(indexName, queryService)) {
            context.getResultSender().lastResult(new CliFunctionResult(memberId, xmlEntity));
          } else {
            context.getResultSender().lastResult(new CliFunctionResult(memberId, false, CliStrings.format(CliStrings.DESTROY_INDEX__INDEX__NOT__FOUND, indexName)));
          }
        }
      }
    } catch (CacheClosedException e) {
      context.getResultSender().lastResult(new CliFunctionResult(memberId, e, e.getMessage()));
    } catch (Exception e) {
      context.getResultSender().lastResult(new CliFunctionResult(memberId, e, e.getMessage()));
    }
  }

  /***
   * 
   * @param name
   * @param queryService
   * @return true if the index was found and removed/false if the index was not found.
   */
  private boolean removeIndexByName(String name, QueryService queryService) {
    List<Index> indexes = (List<Index>) queryService.getIndexes();
    boolean removed = false;
    
    if (indexes != null) {
      for (Index index : indexes) {
        if (index.getName().equals(name)) {
          queryService.removeIndex(index);
          removed = true;
        }
      }
    } 
    return removed;
  }

  @Override
  public String getId() {
    return DestroyIndexFunction.class.getName();
  }

}
