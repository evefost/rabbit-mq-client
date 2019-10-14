package com.eve.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Class扫描工具类
 *
 * @author 卢益
 * @version 1.0.0 on 2017/8/7
 */
public class ClassScaner implements ResourceLoaderAware {

    private static Logger LOGGER = LoggerFactory.getLogger(ClassScaner.class);

    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    private final List<TypeFilter> includeFilters = new LinkedList<TypeFilter>();

    private final List<TypeFilter> excludeFilters = new LinkedList<TypeFilter>();

    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(
            this.resourcePatternResolver);

    public ClassScaner() {

    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils
                .getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(
                resourceLoader);
    }

    public final ResourceLoader getResourceLoader() {
        return this.resourcePatternResolver;
    }

    public void addIncludeFilter(TypeFilter includeFilter) {
        this.includeFilters.add(includeFilter);
    }

    public void addExcludeFilter(TypeFilter excludeFilter) {
        this.excludeFilters.add(0, excludeFilter);
    }

    public void resetFilters(boolean useDefaultFilters) {
        this.includeFilters.clear();
        this.excludeFilters.clear();
    }

    @SafeVarargs
    public static List<Class<?>> scan(String basePackage,
                                      Class<? extends Annotation>... annotations) {
        ClassScaner cs = new ClassScaner();
        if (annotations != null) {
            for (Class<? extends Annotation> anno : annotations) {
                cs.addIncludeFilter(new AnnotationTypeFilter(anno));
            }
        }
        return cs.doScan(basePackage);
    }

    @SafeVarargs
    public static List<Class<?>> scan(String[] basePackages,
                                      Class<? extends Annotation>... annotations) {
        ClassScaner cs = new ClassScaner();
        if (annotations != null) {
            for (Class<? extends Annotation> anno : annotations) {
                cs.addIncludeFilter(new AnnotationTypeFilter(anno));
            }
        }
        List<Class<?>> classes = new ArrayList<Class<?>>();
        List<Class<?>> classesTemp = null;
        for (String s : basePackages) {
            classesTemp = cs.doScan(s);
            if (classesTemp != null) {
                for (Class<?> clazz : classesTemp) {
                    if (!classes.contains(clazz)) {
                        classes.add(clazz);
                    }
                }
            }
        }
        return classes;
    }

    public List<Class<?>> doScan(String basePackage) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        try {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + org.springframework.util.ClassUtils
                    .convertClassNameToResourcePath(SystemPropertyUtils
                            .resolvePlaceholders(basePackage))
                    + "/**/*.class";
            Resource[] resources = this.resourcePatternResolver
                    .getResources(packageSearchPath);

            for (int i = 0; i < resources.length; i++) {
                Resource resource = resources[i];
                if (resource.isReadable()) {
                    MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                    if ((includeFilters.size() == 0 && excludeFilters.size() == 0) || matches(metadataReader)) {
                        try {
                            classes.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
                        } catch (ClassNotFoundException e) {
                            //e.printStackTrace();
                            LOGGER.error(e.getMessage(), e);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            throw new BeanDefinitionStoreException(
                    "I/O failure during classpath scanning", ex);
        }
        return classes;
    }

    protected boolean matches(MetadataReader metadataReader) throws IOException {
        for (TypeFilter tf : this.excludeFilters) {
            if (tf.match(metadataReader, this.metadataReaderFactory)) {
                return false;
            }
        }
        for (TypeFilter tf : this.includeFilters) {
            if (tf.match(metadataReader, this.metadataReaderFactory)) {
                return true;
            }
        }
        return false;
    }

}  