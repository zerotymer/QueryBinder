package Testing;

import QueryBinder.Annotation.BindingMapperParam;
import QueryBinder.Annotation.BindingMapperUrl;
import QueryBinder.QueryResponsible;

import java.util.Date;

@BindingMapperUrl("")
public class MyBoardVO implements QueryResponsible {
    /// FIELDs
    @BindingMapperParam(value = "BID", required = true)
    private int bid;
    @BindingMapperParam(value = "TITLE", required = true)
    private String title;
    @BindingMapperParam(value = "CONTENT", required = true)
    private String content;
    @BindingMapperParam(value = "WRITER", required = true)
    private String writer;
    @BindingMapperParam(value = "CREATE_DATE", required = true)
    private Date createDate;
    @BindingMapperParam(value = "UPDATE_DATE", required = true)
    private Date updateDate;
    @BindingMapperParam(value = "DEL_YN", required = true)
    private char delYN;

    /// CONSTRUCTORs


    /// METHODs
    // Getters
    public int getBid() { return bid; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getWriter() { return writer; }
    public Date getCreateDate() { return createDate; }
    public Date getUpdateDate() { return updateDate; }
    public char getDelYN() { return delYN; }

    // Setters
    public void setBid(int bid) { this.bid = bid; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setWriter(String writer) { this.writer = writer; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
    public void setDelYN(char delYN) { this.delYN = delYN; }

    @Override
    public String toString() {
        return "MyBoardVO{" +
                "bid=" + bid +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", writer='" + writer + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", delYN=" + delYN +
                '}';
    }


}
