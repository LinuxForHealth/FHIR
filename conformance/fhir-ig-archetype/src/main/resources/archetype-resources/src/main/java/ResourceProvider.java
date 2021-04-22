package $package;

import com.ibm.fhir.registry.util.PackageRegistryResourceProvider;

public class MCODEResourceProvider extends PackageRegistryResourceProvider {
    @Override
    public String getPackageId() {
        return "$fhir";
    }
}

