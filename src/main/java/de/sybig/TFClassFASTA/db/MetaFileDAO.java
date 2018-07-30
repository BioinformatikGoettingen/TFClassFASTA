package de.sybig.TFClassFASTA.db;

import java.util.List;

import org.hibernate.SessionFactory;

import de.sybig.TFClassFASTA.core.MetaFile;
import io.dropwizard.hibernate.AbstractDAO;

public class MetaFileDAO extends AbstractDAO<MetaFile> {

    public MetaFileDAO(SessionFactory Factory) {
        super(Factory);
    }

    public MetaFile getByUID(Long uid) {
        return get(uid);
    }

    public List<MetaFile> getByTFClassID(String tfclassID, String align, String type, Long version) {
        return list(namedQuery("MetaFile.getByTFCLASS")
                .setParameter("TFCLASSID", tfclassID)
                .setParameter("ALIGNMENT", MetaFile.Alignment.getEnum(align))
                .setParameter("TYPE", MetaFile.Type.getEnum(type))
                .setParameter("VERSION", version));
    }

    public List<MetaFile> getNewestByTFClassID(String tfclassID, String align, String type) {
        return list(namedQuery("MetaFile.getNewestByTFCLASS")
                .setParameter("TFCLASSID", tfclassID)
                .setParameter("ALIGNMENT", MetaFile.Alignment.getEnum(align))
                .setParameter("TYPE", MetaFile.Type.getEnum(type)).setMaxResults(1));
    }

    public MetaFile getSingleResult(String tfcID, String alignment, String type) {
        return (MetaFile) namedQuery("MetaFile.getSingleResult")
                .setParameter("TFCLASSID", tfcID)
                .setParameter("ALIGNMENT", MetaFile.Alignment.getEnum(alignment))
                .setParameter("TYPE", MetaFile.Type.getEnum(type)).getSingleResult();
    }

    public MetaFile create(MetaFile file) {
        return persist(file);
    }

}
