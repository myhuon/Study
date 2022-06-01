package mobile.exam.network.sample.openapi_with_parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;


public class NaverBookXmlParser {

    // XML에서 읽어들일 태그를 구분한 enum
    private enum TagType { NONE, TITLE, IMAGE, AUTHOR, PUBLISHER};

    // parsing 대상인 tag를 상수로 선언
    private final static String FAULT_RESULT = "fault result";
    private final static String ITEM_TAG = "item";
    private final static String TITLE_TAG = "title";
    private final static String IMAGE_TAG = "image";
    private final static String AUTHOR_TAG = "author";
    private final static String PUBLISHER_TAG = "publisher";

    private XmlPullParser parser;

    public NaverBookXmlParser() {
        try {
            // XmlPullParser 객체 생성
            parser = XmlPullParserFactory.newInstance().newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<NaverBookDto> parse(String xml) {
        ArrayList<NaverBookDto> resultList = new ArrayList<>();
        NaverBookDto dto = null;
        TagType tagType = TagType.NONE;

        try{
            parser.setInput(new StringReader(xml)); // 읽어올 xml String으로 전환 후 parser로 넘김
            int eventType = parser.getEventType(); // 태그 유형 구분

            // xml 문서의 마지막 태그가 아니면 반복 수행
            while(eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName(); // 태그 이름 반환
                        if(tag.equals(ITEM_TAG)){
                            dto = new NaverBookDto(); // 아이템이 시작되면 dto 객체 생성
                        }else if(tag.equals(TITLE_TAG)){
                            if(dto != null){ // title 태그가 item 밖에도 있으니, dto 객체가 생성되어있으면 item 안의 title 태그임을 확인한다.
                                tagType = TagType.TITLE;
                            }
                        }else if(tag.equals(IMAGE_TAG)){
                            tagType = TagType.IMAGE;
                        }else if(tag.equals(AUTHOR_TAG)){
                            tagType = TagType.AUTHOR;
                        }else if(tag.equals(PUBLISHER_TAG)){
                            tagType = TagType.PUBLISHER;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals(ITEM_TAG)){
                            resultList.add(dto);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch (tagType){
                            case TITLE:
                                dto.setTitle(parser.getText());
                                break;
                            case IMAGE:
                                dto.setImageLink(parser.getText());
                                break;
                            case AUTHOR:
                                dto.setAuthor(parser.getText());
                                break;
                            case PUBLISHER:
                                dto.setPublisher(parser.getText());
                                break;
                        }
                        tagType = TagType.NONE; // 다음 태그로 넘어가기 위해 none으로 다시 초기화
                        break;
                }
                // 다음 태그로 넘어간다
                eventType = parser.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return resultList;
    }
}
