package com.github.opengl8080.gradle.plugin.eclipse.annotationprocessing;

import org.gradle.api.Plugin
import org.gradle.api.Project

class Ape implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('ape', ApeConfig)
        project.apply(plugin: 'eclipse')
        
        def eclipseAptPrefsFile = '.settings/org.eclipse.jdt.apt.core.prefs'
        def eclipseFactoryPathFile = '.factorypath'
        
        project.eclipse {
            classpath.file.withXml {
                it.asNode().appendNode('classpathentry', [kind: 'src', path: '.apt_generated'])
            }
        
            jdt.file.withProperties { properties ->
                properties.put 'org.eclipse.jdt.core.compiler.processAnnotations', 'enabled'
            }
        }
        
        project.eclipseJdt << {
            project.file(eclipseAptPrefsFile).write """\
                |eclipse.preferences.version=1
                |org.eclipse.jdt.apt.aptEnabled=true
                |org.eclipse.jdt.apt.genSrcDir=.apt_generated
                |org.eclipse.jdt.apt.reconcileEnabled=true
                |""".stripMargin()
            
            def processorJarPattern = project.ape.processorJarPattern
            File processorJar;
            
            if (processorJarPattern instanceof String) {
                processorJar = project.configurations
                                      .compile
                                      .find {it.name =~ processorJarPattern}
            } else if (processorJarPattern instanceof Closure) {
                processorJar = processorJarPattern()
            } else {
                throw new IllegalArgumentException("processorJarPattern must be jar pattern (String) or jar file (File). but processorJarPattern = ${processorJarPattern}")
            }
            
            if (processorJar == null) {
                throw new FileNotFoundException("processor jar is not found. processorJarPattern = '${processorJarPattern}'.")
            }
            
            def processorJarPath = processorJar.absolutePath
            
            project.file(eclipseFactoryPathFile).write """\
                |<factorypath>
                |    <factorypathentry kind="PLUGIN" id="org.eclipse.jst.ws.annotations.core" enabled="true" runInBatchMode="false"/>
                |    <factorypathentry kind="EXTJAR" id="${project.file(processorJarPath).absolutePath}" enabled="true" runInBatchMode="false"/>
                |</factorypath>
                |""".stripMargin()
        }
        
        project.cleanEclipse << {
            project.file(eclipseAptPrefsFile).delete()
            project.file(eclipseFactoryPathFile).delete()
        }
    }
}
