package com.saurabh.netflixapi.service;

import com.saurabh.netflixapi.model.Node;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class JSoupService {


    /**
     * Fetches all the anchor tags from the given URL and returns them as a list of elements.
     *
     * @param url the URL to fetch the anchor tags from
     * @return a list of anchor tags as Jsoup elements
     * @throws IOException if there was an error fetching the URL or parsing the HTML
     */
    public List<Element> getAnchorTags(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements anchorTags = doc.select("a[href]");
        return new ArrayList<>(anchorTags);
    }



    // generate docs for the function below

    public List<String> getFolderLinksFromALink(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        List<String> resultList = new ArrayList<>();

        for (var link : links) {
            String href = link.attr("href");
            String fullLink = link.absUrl("href");
            if (!fullLink.isEmpty()) {
                resultList.add(fullLink);
            } else {
                resultList.add(href);
            }
        }

        return resultList;
    }

    public List<Node> getListOfNodesFromLink() {
        String url = "https://sv3.hivamovie.com/new/Movie/";
        String[] batches = new String[] {""};
        List<Node> resultList = new ArrayList<>();

        for (String batchName: batches) {
            Document doc = getDocument(url + batchName);
            if (doc != null) {
                Elements links = doc.select("a[href]");
                addNodesFromLinks(links, resultList);
            }
        }

        return resultList;
    }

    private Document getDocument(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addNodesFromLinks(Elements links, List<Node> resultList) {
        for (Element link : links) {
            String decodedUrl = decodeUrl(link.attr("href"));
            Matcher matcher = getMatcher(decodedUrl);

            while (matcher.find()) {
                String rawFileName = matcher.group(0);
                String movieName = matcher.group(1).replace(".", " ");
                if (rawFileName.contains("srt")) {
                    addSubtitleToPreviousMovie(movieName, link, resultList);
                } else {
                    addNewMovieNode(rawFileName, movieName, link, resultList);
                }
            }
        }
    }

    private String decodeUrl(String url) {
        String decodedUrl = "";
        try {
            decodedUrl = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodedUrl;
    }

    private Matcher getMatcher(String decodedUrl) {
        String regex = "([\\.\\w']+?)(\\.[0-9]{4}\\..*)";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(decodedUrl);
    }

    private void addSubtitleToPreviousMovie(String movieName, Element link, List<Node> resultList) {
        if (!resultList.isEmpty() && resultList.get(resultList.size() - 1).getMovieName().equals(movieName)) {
            Node previousMovie = resultList.get(resultList.size() - 1);
            previousMovie.setStrLink(link.baseUri() + link.attr("href"));
        }
    }

    private void addNewMovieNode(String rawFileName, String movieName, Element link, List<Node> resultList) {
        Node node = Node.builder()
                .rawMovieName(rawFileName)
                .movieName(movieName)
                .text(link.text())
                .link(link.baseUri() + link.attr("href"))
                .build();
        resultList.add(node);
    }


}
