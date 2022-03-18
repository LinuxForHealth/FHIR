/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.config.test;

import static org.testng.Assert.assertEquals;

import java.util.Set;

import org.testng.annotations.Test;

import com.ibm.fhir.config.Interaction;
import com.ibm.fhir.config.PropertyGroup;
import com.ibm.fhir.config.ResourcesConfigAdapter;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class ResourcesConfigAdapterTest {
  @Test
  public void testGetSupportedResourceTypes() throws Exception {
      JsonObject json = Json.createObjectBuilder().build();
      PropertyGroup pg = new PropertyGroup(json);
      ResourcesConfigAdapter resourcesConfigAdapter = new ResourcesConfigAdapter(pg);

      Set<String> supportedResourceTypes = resourcesConfigAdapter.getSupportedResourceTypes();
      assertEquals(supportedResourceTypes.size(), 141);

      System.out.println(supportedResourceTypes);

      for (Interaction interaction : Interaction.values()) {
          supportedResourceTypes = resourcesConfigAdapter.getSupportedResourceTypes(interaction);
          assertEquals(supportedResourceTypes.size(), 141);
      }
  }
}
