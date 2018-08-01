package de.sybig.TFClassFASTA.db;

import java.util.List;
import org.hibernate.SessionFactory;
import de.sybig.TFClassFASTA.core.Fasta;
import de.sybig.TFClassFASTA.core.MetaFile;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.query.Query;

public class FastaDAO extends AbstractDAO<Fasta> {

    public FastaDAO(SessionFactory factory) {
        super(factory);
    }

    public Fasta getByUID(Long uid) {
        return get(uid);
    }

    public Fasta create(Fasta fst) {
        return persist(fst);
    }

    public List<Fasta> getByFile(MetaFile file, String taxon) {
        return list(namedQuery("Fasta.getByFile").setParameter("FILE", file).setParameter("TAXON", taxon));
    }

    public List<Fasta> getLevel5(String tfcID, String type, String alignment) {
//        Query level = namedQuery("Fasta.levels").setParameter("TFCID", tfcID)
//                .setParameter("TYPE", MetaFile.Type.getEnum(type))
//                .setParameter("ALIGNMENT", MetaFile.Alignment.getEnum(alignment));
//        System.out.println("level " + level);
        return list(namedQuery("Fasta.getLevel5")
                .setParameter("TFCID", tfcID)
                .setParameter("TYPE", MetaFile.Type.getEnum(type))
                .setParameter("ALIGNMENT", MetaFile.Alignment.getEnum(alignment)));
    }
}
