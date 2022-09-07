//import java.net.URL
//import javax.net.ssl.HttpsURLConnection
//
//@Suppress("NULLABLE_TYPE_PARAMETER_AGAINST_NOT_NULL_TYPE_PARAMETER")
//fun <T, R> Provider<T>.mapNotNull(transform: (T) -> R): Provider<R> =
//    map(transform)
//
//@DisableCachingByDefault
//open class CustomWrapper @Inject constructor(objects: ObjectFactory) : Wrapper() {
//    @get:Optional
//    @get:Input
//    val githubOwner = objects.property<String>().convention("Fleshgrinder")
//
//    @get:Optional
//    @get:Input
//    val githubRepo = objects.property<String>().convention("custom-gradle-distribution")
//
//    @get:Input
//    val baseUrl = objects.property<String>().convention(githubOwner.zip(githubRepo) { user, repo ->
//        "https://github.com/$user/$repo"
//    })
//
//    private val version = objects.property<String>().convention(baseUrl.mapNotNull { baseUrl ->
//        null
////        (URL("$baseUrl/releases/latest").openConnection() as HttpsURLConnection).apply {
////            requestMethod = "HEAD"
////            instanceFollowRedirects = false
////            connectTimeout = 1_000
////            readTimeout = 1_000
////            useCaches = false
////        }.takeIf { it.responseCode == 302 }
////            ?.getHeaderField("location")
////            ?.substringAfterLast('/')
//    })
//
//    private val sha256 = objects.property<String>().convention(baseUrl.zip(version) { baseUrl, version ->
//        URL("$baseUrl/releases/download/$version/gradle.zip.sha256")
//            .openStream()
//            .readBytes()
//            .toString(Charsets.US_ASCII)
//            .trim()
//    })
//
//    private val url = objects.property<String>().convention(baseUrl.zip(version) { baseUrl, version ->
//        "$baseUrl/releases/download/$version/gradle.zip"
//    })
//
//    override fun getDistributionUrl(): String? =
//        url.orNull
//
//    override fun setDistributionUrl(url: String?) {
//        this.url.set(url)
//        super.setDistributionUrl(url)
//    }
//
//    override fun getDistributionSha256Sum(): String? =
//        sha256.orNull
//
//    override fun setDistributionSha256Sum(sha256: String?) {
//        this.sha256.set(sha256)
//        super.setDistributionSha256Sum(sha256)
//    }
//
//    override fun getGradleVersion(): String? =
//        version.orNull
//
//    override fun setGradleVersion(version: String?) {
//        this.version.set(version)
//        super.setGradleVersion(version)
//    }
//}
//
//tasks.replace("wrapper", CustomWrapper::class)
