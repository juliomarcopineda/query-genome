import java.io.FilenameFilter

val excludes = listOf(".gradle", ".settings", "bin", ".git", ".idea", "build", "cert", "gradle", "codeFormatter")

val dirFilter = FilenameFilter { dir, name -> File(dir, name).isDirectory }

File(rootDir.absolutePath).list(dirFilter).forEach {
    if (!excludes.contains(it)) {
        include(it)
    }
}

rootProject.name = "query-genome"
