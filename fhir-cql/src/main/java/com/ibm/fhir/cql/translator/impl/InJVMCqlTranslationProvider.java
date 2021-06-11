package com.ibm.fhir.cql.translator.impl;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.cqframework.cql.cql2elm.CqlTranslator;
import org.cqframework.cql.cql2elm.CqlTranslator.Options;
import org.cqframework.cql.cql2elm.CqlTranslatorException;
import org.cqframework.cql.cql2elm.FhirLibrarySourceProvider;
import org.cqframework.cql.cql2elm.LibraryBuilder;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.LibrarySourceProvider;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.Library;
import org.fhir.ucum.UcumService;
import org.opencds.cqf.cql.engine.execution.CqlLibraryReader;

import com.ibm.fhir.cql.translator.CqlTranslationException;


public class InJVMCqlTranslationProvider extends BaseCqlTranslationProvider {

    private static Logger LOG = Logger.getLogger(InJVMCqlTranslationProvider.class.getName());
    
    private ModelManager modelManager;
    private LibraryManager libraryManager;
    
    public InJVMCqlTranslationProvider() {
        this.modelManager = new ModelManager();
        this.libraryManager = new LibraryManager(modelManager);
        addLibrarySourceProvider(new FhirLibrarySourceProvider());
    }

    public InJVMCqlTranslationProvider(LibraryManager libraryManager, ModelManager modelManager) {
        this.modelManager = modelManager;
        this.libraryManager = libraryManager;
    }

    public InJVMCqlTranslationProvider(LibrarySourceProvider provider) {
        this();
        addLibrarySourceProvider(provider);
    }

    public InJVMCqlTranslationProvider addLibrarySourceProvider(LibrarySourceProvider provider) {
        libraryManager.getLibrarySourceLoader().registerProvider(provider);
        return this;
    }

    @Override
    public List<Library> translate(InputStream cql, List<Option> options, Format targetFormat) throws CqlTranslationException {
        List<Library> result = new ArrayList<>();

        UcumService ucumService = null;
        LibraryBuilder.SignatureLevel signatureLevel = LibraryBuilder.SignatureLevel.None;

        List<Options> optionsList;
        if (options != null) {
            optionsList = options.stream().map( o -> CqlTranslator.Options.valueOf( o.name() ) ).collect(Collectors.toList());
        } else {
            optionsList = Collections.emptyList();
        }

        try {
            CqlTranslator translator =
                    CqlTranslator.fromStream(cql, modelManager, libraryManager, ucumService, CqlTranslatorException.ErrorSeverity.Info, signatureLevel, optionsList.toArray(new Options[optionsList.size()]));
    
            LOG.info( String.format("Translated CQL contains %d errors, %d exceptions", translator.getErrors().size(), translator.getExceptions().size()) );
            
            List<CqlTranslatorException> badStuff = new ArrayList<>();
            badStuff.addAll(translator.getErrors());
            badStuff.addAll(translator.getExceptions());
            if (!badStuff.isEmpty()) {
                throw new CqlTranslationException("CQL translation contained failed", badStuff);
            }
    
            switch (targetFormat) {
            case XML:
                Library fromStream = CqlLibraryReader.read(new StringReader(translator.toXml()));
                result.add(fromStream);
                
                // Anything else that was translated via includes and the LibrarySourceProvider
                // interface will get returned as well
                for( Map.Entry<String,String> entry : translator.getLibrariesAsXML().entrySet() ) {
                    Library dependency = CqlLibraryReader.read(new StringReader(entry.getValue()));
                    result.add(dependency);
                }   
                break;
            default:
                throw new CqlTranslationException(String.format("The CQL Engine does not support format %s", targetFormat.name()));
            }
        } catch( CqlTranslationException cqex ) {
            throw cqex;
        } catch( Exception ex ) {
            throw new CqlTranslationException("CQL translation failed", ex);
        }

        return result;
    }
}
