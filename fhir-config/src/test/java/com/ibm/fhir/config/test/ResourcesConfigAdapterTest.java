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
import com.ibm.fhir.core.FHIRVersionParam;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class ResourcesConfigAdapterTest {
  @Test
  public void testGetSupportedResourceTypes_r4() throws Exception {
      JsonObject json = Json.createObjectBuilder().build();
      PropertyGroup pg = new PropertyGroup(json);
      ResourcesConfigAdapter resourcesConfigAdapter = new ResourcesConfigAdapter(pg, FHIRVersionParam.VERSION_40);

      Set<String> supportedResourceTypes = resourcesConfigAdapter.getSupportedResourceTypes();
      assertEquals(supportedResourceTypes.size(), 125);

      for (Interaction interaction : Interaction.values()) {
          supportedResourceTypes = resourcesConfigAdapter.getSupportedResourceTypes(interaction);
          assertEquals(supportedResourceTypes.size(), 125);
      }
  }

  @Test
  public void testGetSupportedResourceTypes_r4b() throws Exception {
      JsonObject json = Json.createObjectBuilder().build();
      PropertyGroup pg = new PropertyGroup(json);
      ResourcesConfigAdapter resourcesConfigAdapter = new ResourcesConfigAdapter(pg, FHIRVersionParam.VERSION_43);

      Set<String> supportedResourceTypes = resourcesConfigAdapter.getSupportedResourceTypes();
      assertEquals(supportedResourceTypes.size(), 141);

      for (Interaction interaction : Interaction.values()) {
          supportedResourceTypes = resourcesConfigAdapter.getSupportedResourceTypes(interaction);
          assertEquals(supportedResourceTypes.size(), 141);
      }
  }
}
