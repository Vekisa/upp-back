package naucna_centrala.nc.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Magazine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    private String naziv;
    @Column
    private String issn;
    @Column
    private String nacin_placanja;
    @Column
    private String editor1;
    @Column
    private String editor2;
    @Column
    private String reviewer1;
    @Column
    private String reviewer2;
    @Column
    private Boolean placaAutor;
    @Column
    private Boolean aktivan;
    @ManyToOne
    private CustomUser glavni_urednik;
    @ManyToMany
    private List<CustomUser> imaClanarinu;
    @ManyToMany
    private List<ScientificArea> scientificAreaList;
    @OneToMany(mappedBy = "magazine")
    private List<Labor> labors;

    public Magazine() {
        imaClanarinu = new ArrayList<>();
        scientificAreaList = new ArrayList<>();
        labors = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getNacin_placanja() {
        return nacin_placanja;
    }

    public void setNacin_placanja(String nacin_placanja) {
        this.nacin_placanja = nacin_placanja;
    }

    public List<ScientificArea> getScientificAreaList() {
        return scientificAreaList;
    }

    public void setScientificAreaList(List<ScientificArea> scientificAreaList) {
        this.scientificAreaList = scientificAreaList;
    }

    public CustomUser getGlavni_urednik() {
        return glavni_urednik;
    }

    public void setGlavni_urednik(CustomUser glavni_urednik) {
        this.glavni_urednik = glavni_urednik;
    }

    public String getEditor1() {
        return editor1;
    }

    public void setEditor1(String editor1) {
        this.editor1 = editor1;
    }

    public String getEditor2() {
        return editor2;
    }

    public void setEditor2(String editor2) {
        this.editor2 = editor2;
    }

    public String getReviewer1() {
        return reviewer1;
    }

    public void setReviewer1(String reviewer1) {
        this.reviewer1 = reviewer1;
    }

    public String getReviewer2() {
        return reviewer2;
    }

    public void setReviewer2(String reviewer2) {
        this.reviewer2 = reviewer2;
    }

    public Boolean getAktivan() {
        return aktivan;
    }

    public void setAktivan(Boolean aktivan) {
        this.aktivan = aktivan;
    }

    public Boolean getPlacaAutor() {
        return placaAutor;
    }

    public void setPlacaAutor(Boolean placaAutor) {
        this.placaAutor = placaAutor;
    }

    public List<CustomUser> getImaClanarinu() {
        return imaClanarinu;
    }

    public void setImaClanarinu(List<CustomUser> imaClanarinu) {
        this.imaClanarinu = imaClanarinu;
    }

    public List<Labor> getLabors() {
        return labors;
    }

    public void setLabors(List<Labor> labors) {
        this.labors = labors;
    }
}
