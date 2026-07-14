package app.linksnap.features.link.data

import app.linksnap.models.SummarizedData
import com.google.genai.Client
import org.jsoup.Jsoup

object LinkSummarizer {

    private val API_KEY = System.getenv("LINKSNAP_GEMINI_API_KEY")
    private val client = if (!API_KEY.isNullOrBlank()) {
        try {
            Client.builder().apiKey(API_KEY)
                .build()
        } catch (e: Exception) {
            null
        }
    } else {
        null
    }

    suspend fun summarize(url: String): SummarizedData {

        val doc = try {
            val data = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0: Win64: x64) AppleKit/537.36 (KHTML, like Gecko) Chrome/91.04472.124)")
                .timeout(10000)
                .get()
            data
        } catch (e: Exception) {
            null
        }

        val title = doc?.title() ?: "No Title"
        val imageUrl = doc?.select("meta[property=org:image]")?.attr("content")?.takeIf {
            it.isNotBlank()
        }
        val bodyText =  doc?.body()?.text()?.take(5000) ?: ""


        val prompt = """
            You are an expert content curator. Analyze the following article and provide structured metadata for a high-end bookmarking app.
            Provide the output in this EXACT format:
            
            SUMMARY: [A concise 3-sentence summary highlighting the "what" and "why" for a preview card]
            DESCRIPTION: [A deep-dive analysis consisting of 2 insightful paragraphs. Explain the core value and key takeaways for the reader]
            CATEGORY: [One word best fitting: Technology, Design, Business, Health, Science, Education, or General]
            TAGS: [3-5 descriptive keywords separated by commas]
            
            Article Title: $title
            Content: $bodyText
        """.trimIndent()


        var aiSummary = ""
        var desc = ""
        var category = ""
        var tags = ""

        try {
            if (client != null) {
                val genRes = client.models.generateContent("gemini-2.5-flash", prompt, null)
                val rawText = genRes.text() ?: ""

                if (rawText.uppercase().contains("SUMMARY:")) {
                    aiSummary = extract(rawText, "SUMMARY:","DESCRIPTION:")
                    desc = extract(rawText, "DESCRIPTION:","CATEGORY:")
                    category = extract(rawText, "CATEGORY:","TAGS:")
                    tags = extract(rawText, "TAGS:",null)
                }

            } else {
             //No API KEY FOUND
            }

        } catch (e: Exception) {

        }

        return SummarizedData(
            title = title,
            imageUrl = imageUrl,
            aiSummary = aiSummary,
            category = category.ifBlank { "General" },
            tags = tags.ifBlank { "link" },
            bodySnippet = desc,
        )

    }

    fun extract(text: String, startTag: String, endTag: String?): String {
        val startIndex = text.indexOf(startTag, ignoreCase = true)

        if (startIndex == -1) return ""
        val contentStart = startIndex + startTag.length
        val endIndex = if (endTag?.isEmpty() == true) {
            text.length
        } else {
            endTag?.let { text.indexOf(it, contentStart, ignoreCase = true) }.takeIf { it != -1 } ?: text.length
        }
        return text.substring(contentStart, endIndex).trim()
    }


}