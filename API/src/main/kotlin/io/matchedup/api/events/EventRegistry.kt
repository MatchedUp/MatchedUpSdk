package io.matchedup.api.events

import kotlinx.serialization.*
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.net.URLDecoder
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile

object EventRegistry {

    private val events = HashMap<Class<out IEvent>, String>()
    private val serializers = HashMap<String, KSerializer<out IEvent>>()

    init {
        findEvents()
    }

    fun get(eventClass: Class<out IEvent>) = events[eventClass]

    fun getSerializer(eventType: String) = serializers[eventType]

    private fun findEvents() {
        println("Finding Events")
        for (c in getClassesForPkg("io.matchedup")!!) {
            if (
                c.annotations.firstOrNull {
                    it.annotationClass.java.name.equals(Event::class.java.name)
                } != null
            ) {
                val clazz = Class.forName(c.name) as Class<out IEvent>
                val event = clazz.annotations.find { it is Event } as? Event
                if (event?.type == null) continue

                val eventType = event.type
                val serializer = getSerializer(clazz)

                events[clazz] = eventType
                serializers[eventType] = serializer

                println("  > Registered: $eventType -> ${c.name}")
            }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun getSerializer(c: Class<*>): KSerializer<out IEvent> {
        c.annotations.find { it is Serializable } as? Serializable
            ?: throw Error("Event '${Class.forName(c.name)}' must contain the @Serializable annotation")

        return serializer(c) as KSerializer<out IEvent>
    }

    @Throws(ClassNotFoundException::class)
    private fun getClassesForPkg(packageToSearch: String): List<Class<*>>? {
        val directories = ArrayList<File>()
        val packagePath = packageToSearch.replace('.', '/')
        try {
            val cld = javaClass.classLoader
            // Ask for all resources for the path
            val resources: Enumeration<URL> = cld.getResources(packagePath)
            while (resources.hasMoreElements()) {
                directories.add(File(URLDecoder.decode(resources.nextElement().path, "UTF-8")))
            }
        } catch (e: Exception) {
            throw ClassNotFoundException("$packageToSearch is not a valid package to search. $e")
        }
        val classes = ArrayList<Class<*>>()

        for (dir in directories) {
            val jarIndex = dir.path.indexOf(".jar!")
            if (jarIndex >= 0) {
                getJarClasses(classes, packagePath, dir.path)
            } else {
                dfsForClasses(classes, packagePath, dir)
            }
        }
        return classes
    }

    private fun dfsForClasses(classes: ArrayList<Class<*>>, packageToSearch: String, dir: File) {
        if (dir.exists()) {
            val files = dir.listFiles()
            for (file in files) {
                if (file.isDirectory) {
                    dfsForClasses(classes, packageToSearch, file)
                } else {
                    if (file.path.endsWith(".class")) {
                        try {
                            val startIndex =
                                file.path.indexOf(packageToSearch.replace("/", "\\")) // match file.path dir breaks
                            if (startIndex >= 0) { // if not the package we want, skip
                                val classPath = file.path.substring(startIndex)
                                val classFile = classPath.substring(0, classPath.length - ".class".length)
                                classes.add(Class.forName(classFile))
                            }
                        } catch (_: NoClassDefFoundError) {
                        }
                    }
                }
            }
        }
    }

    private fun getJarClasses(classes: ArrayList<Class<*>>, packageToSearch: String, path: String) {
        val jarIndex = path.indexOf(".jar!")
        var startIndex = 0
        if (path.startsWith("file:\\")) {
            startIndex = "file:\\".length
        } else if (path.startsWith("file://")) {
            startIndex = "file:/".length
        } else if (path.startsWith("file:/")) {
            startIndex = "file:".length
        }
        val jarPath = path.substring(startIndex, jarIndex + ".jar".length)

        val jarFile = JarFile(jarPath)
        val jarEntries: Enumeration<JarEntry> = jarFile.entries()
        val jarUrls = arrayOf(URL("jar:file:$jarPath!/"))
        val cl = URLClassLoader.newInstance(jarUrls)

        while (jarEntries.hasMoreElements()) {
            val je: JarEntry = jarEntries.nextElement()
            if (!je.name.startsWith(packageToSearch) || je.isDirectory || !je.name.endsWith(".class")) {
                continue
            }

            try {
                var className = je.name.substring(0, je.name.length - ".class".length)
                className = className.replace('/', '.')
                classes.add(cl.loadClass(className))
            } catch (_: NoClassDefFoundError) {
            }
        }
    }

}