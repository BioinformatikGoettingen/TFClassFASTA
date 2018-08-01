package de.sybig.TFClassFASTA.core;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "Fasta.getByFile", query = "Select fst FROM "
    		+ "Fasta fst where fst.file = :FILE "
    		+ "AND (:TAXON is null OR fst.taxon = :TAXON)"),
    @NamedQuery(name = "Fasta.getLevel5", query = "SELECT fst "
            + "FROM Fasta fst JOIN fst.file meta "
            + "WHERE fst.tfactor = :TFCID "
            + "AND meta.type = :TYPE "
            + "AND meta.alignment = :ALIGNMENT "
            + "AND LENGTH(meta.tfclassID) =  (SELECT MAX(LENGTH(meta2.tfclassID)) FROM Fasta fst2 JOIN fst2.file meta2 "
            + "  WHERE fst2.tfactor = :TFCID "
            + "  AND meta2.type = :TYPE "
            + "  AND meta2.alignment = :ALIGNMENT)" ),
    @NamedQuery(name = "levels", query = "SELECT MAX(LENGTH(meta2.tfclassID)) FROM Fasta fst2 JOIN fst2.file meta2 "
            + "  WHERE fst2.tfactor = :TFCID "
            + "  AND meta2.type = :TYPE "
            + "  AND meta2.alignment = :ALIGNMENT" )
})
public class Fasta {
	
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)	
	private Long UID;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fileID")
    private MetaFile file;
    private String taxon;
    private String tfactor;
    private String sequence;
    private String header;
    
    public Fasta() {
    	
    }
    public Fasta(String header, String seq, MetaFile file) {
    	this.header = header;
    	this.sequence = seq;
    	this.file = file;
    	this.taxon = "Default";
    	this.tfactor = "Default";
    }
    public Fasta(String header, String seq, MetaFile file, String taxon, String tfactor) {
    	this.header = header;
    	this.sequence = seq;
    	this.file = file;
    	this.taxon = taxon; 	
    	this.tfactor = tfactor;
    }
    public Long getUID() {
		return UID;
	}

	public void setUID(Long uID) {
		UID = uID;
	}

	public String getTaxon() {
		return taxon;
	}

	public void setTaxon(String taxon) {
		this.taxon = taxon;
	}

	public String getTFactor() {
		return tfactor;
	}

	public void setTFactor(String tfactor) {
		this.tfactor = tfactor;
	}
	
	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}
	
	public MetaFile getFile() {
		return file;
	}
	
	public void setFile(MetaFile file) {
		this.file = file;
	}
	
	public String toString() {
		return this.header + "\n" + this.sequence;
	}
}
