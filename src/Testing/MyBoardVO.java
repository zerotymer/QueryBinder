package Testing;

import QueryBinder.Annotation.QueryBindingGetParam;
import QueryBinder.Annotation.QueryBindingParam;
import QueryBinder.Annotation.QueryBindingUrl;
import QueryBinder.QueryResponsible;

import java.util.Date;

@QueryBindingUrl("")
public class MyBoardVO implements QueryResponsible {
    /// FIELDs

    private int bid;
    private String title;
    private String content;
    private String writer;
    private Date createDate;
    private Date updateDate;
    private char delYN = 'N';

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
    @QueryBindingGetParam(value = "BID", isRequired = true, defaultValue = "1")
    public void setBid(String bid) { this.bid = Integer.parseInt(bid); }

    @QueryBindingGetParam(value = "TITLE", isRequired = true, defaultValue = "")
    public void setTitle(String title) { this.title = title; }

    @QueryBindingGetParam(value = "CONTENT", isRequired = true, defaultValue = "")
    public void setContent(String content) { this.content = content; }

    @QueryBindingGetParam(value = "WRITER", isRequired = true, defaultValue = "")
    public void setWriter(String writer) { this.writer = writer; }

    public void setCreateDate(Date createDate) { this.createDate = createDate; }
//    @BindingMapperParam(value = "CREATE_DATE", required = true, defaultValue = "")
    public void setCreateDate(String createDate) { this.createDate = new Date(Long.parseLong(createDate)); }

    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
//    @BindingMapperParam(value = "UPDATE_DATE", required = true, defaultValue = "")
    public void setUpdateDate(String updateDate) { this.updateDate = new Date(Long.parseLong(updateDate)); }

    public void setDelYN(char delYN) { this.delYN = delYN; }
    @QueryBindingGetParam(value = "DEL_YN", isRequired = true, defaultValue = "N")
    public void setDelYN(String delYN) { this.delYN = delYN.charAt(0); }

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

    @Override
    public MyBoardVO newInstance() {
        return new MyBoardVO();
    }
}
