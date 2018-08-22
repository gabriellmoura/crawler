package com.data.importer.reader.data;

import com.data.importer.reader.model.Book;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DataImporter {

    /**
     * Method responsible for import a list o Book.
     * @return - Return a list of Books imported from URL <a>https://kotlinlang.org/docs/books.html</a>
     */
    public List<Book> importBook() {
        String URL = "https://kotlinlang.org/docs/books.html";
        Document htmlDocument = getDocumentFromUrl(URL);

        Document doc;
        doc = Jsoup.parse(htmlDocument.html());
        Element article = doc.select("article").first();

        List<Element> titleElements = article.getElementsByTag("h2");
        List<Element> languageElements = article.getElementsByTag("div");
        List<Element> pElements = article.getElementsByTag("p");

        List<Book> books = new ArrayList<>();
        getBooks(titleElements, languageElements, pElements, books);
        findIsbn(books);
        return books;
    }

    private void getBooks(List<Element> titleElements, List<Element> languageElements, List<Element> pElements, List<Book> books) {
        Integer indxParagraph = 2;

        for (int i = 0; i < titleElements.size(); i++) {
            Book book = new Book();

            book.setId(Long.valueOf(i + 1));
            book.setDescription(getTextParagraph(pElements, indxParagraph, book));
            book.setLanguage(getText(languageElements, i));
            book.setTitle(getText(titleElements, i));

            indxParagraph++;
            books.add(book);
        }
    }


    private Document getDocumentFromUrl(String url) {
        Document htmlDocument = null;
        try {
            htmlDocument = Jsoup.connect(url)
                    .header("Accept-Encoding", "gzip, deflate")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                    .maxBodySize(0)
                    .timeout(600000)
                    .get();
        } catch (Exception ex) {

        }
        return htmlDocument;
    }

    private String getText(List<Element> elements, int i) {
        if (elements == null || elements.isEmpty()) {
            return null;
        }
        return elements.get(i).text();
    }

    private String getTextParagraph(List<Element> paragraphElements, Integer indxParagraph, Book book) {
        if (paragraphElements == null || paragraphElements.isEmpty()) {
            return null;
        }
        StringBuilder paragraph = new StringBuilder();
        if (indxParagraph.equals(2)) {

            paragraph.append(paragraphElements.get(0).text())
                    .append(paragraphElements.get(1).text())
                    .append(paragraphElements.get(indxParagraph).text());
            getLinkToBuy(paragraphElements.get(0), book);
        } else {
            paragraph.append(paragraphElements.get(indxParagraph).text());
            getLinkToBuy(paragraphElements.get(indxParagraph), book);
        }

        return paragraph.toString();
    }

    private void getLinkToBuy(Element paragraphElement, Book book) {
        List<Element> linkElements = paragraphElement.getElementsByTag("a");

        if (linkElements != null && !linkElements.isEmpty()) {
            Element linkElement = linkElements.get(0);
            book.setLinkToBuy(linkElement.attr("href"));
        }
    }

    private void findIsbn(List<Book> books) {
        books.forEach(b -> {
            Document htmlDocument = getDocumentFromUrl(b.getLinkToBuy());
            if (htmlDocument != null) {
                String htmlPage = htmlDocument.text();
                Pattern word = Pattern.compile("ISBN");
                Matcher match = word.matcher(htmlPage);
                if (match.find()) {
                    String isbn = htmlPage.substring(match.start(), (match.end() + 19));
                    isbn = isbn.replaceAll("[^-?0-9]+", "");
                    b.setIsbn(isbn);
                } else {
                    b.setIsbn("Unavailable");
                }

            } else {
                b.setIsbn("Unavailable");
            }
        });
    }
}
