package de.sybig.TFClassFASTA.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import de.sybig.TFClassFASTA.core.Fasta;
import de.sybig.TFClassFASTA.core.MetaFile;
import de.sybig.TFClassFASTA.db.FastaDAO;
import de.sybig.TFClassFASTA.db.MetaFileDAO;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.setup.Environment;
import javax.ws.rs.core.Response;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class FastaResource {

	private final FastaDAO fastaDAO;
	private final MetaFileDAO metafileDAO;
	private final Environment environment;
	
	public FastaResource(FastaDAO fastaDAO, MetaFileDAO metafileDAO, Environment environment) {
		this.fastaDAO = fastaDAO;
		this.metafileDAO = metafileDAO;
		this.environment = environment;
	}
	
        
        @GET
        @Produces("application/fasta, application/json")
        @Path("{TFCLASSID:(?:(?:[0-9]*)\\.){2,3}(?:[0-9]+)}/{TYPE: dbd}/{ALIGNMENT: phyml}")
        @UnitOfWork
        public Response getOrigFile(@PathParam(value = "TFCLASSID") String tfcID,
			@PathParam(value = "TYPE") String type,
			@PathParam(value = "ALIGNMENT") String alignment){
            MetaFile metafile = metafileDAO.getSingleResult(tfcID, "Phyml", "DBD");
            List<Fasta> result = fastaDAO.getByFile(metafile, null);if (result.isEmpty()){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(result).build();
            
        }
        @GET
        @Produces("application/fasta, application/json")
        @Path("{TFCLASSID:(?:(?:[0-9]*)\\.){4}(?:[0-9]+)}/{TYPE: dbd}/{ALIGNMENT: phyml}")
        @UnitOfWork
        public Response getOrigFileLevel5(@PathParam(value = "TFCLASSID") String tfcID,
			@PathParam(value = "TYPE") String type,
			@PathParam(value = "ALIGNMENT") String alignment){
            List<Fasta> result = fastaDAO.getLevel5(tfcID, "DBD", "PhyML");
            if (result.isEmpty()){
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(result).build();
        }
        
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/version/{TFCLASSID}/{TYPE}/{ALIGNMENT}")
	@UnitOfWork
	public String getNewestVersion(
			@PathParam(value = "TFCLASSID") String TFClassID,
			@PathParam(value = "TYPE") String Type,
			@PathParam(value = "ALIGNMENT") String Alignment){
		List<MetaFile> result = metafileDAO.getNewestByTFClassID(TFClassID, Alignment, Type);
		return result.get(0).getVersion().toString();
	}
	
	/*@GET
	@Produces("application/fasta")
	@Path("/taxon/{TAXON}")
	@UnitOfWork
	public List<Fasta> getFastaByTaxon(
			@PathParam(value = "TAXON") String Taxons,
			@QueryParam(value = "ALIGN") String Align,
			@QueryParam(value = "TYPE") String Type) {
		List<String> listTaxons = Arrays.asList(Taxons.split(","));
		return listTaxons.stream().flatMap(tax -> fastaDAO.getByTaxon(tax, Type, Align).stream()).collect(Collectors.toList());
	}
	
	@GET
	@Produces("application/fasta")
	@Path("/alignment/{ALIGN}")
	@UnitOfWork
	public List<Fasta> getFastaByAlignment(
			@PathParam(value = "ALIGN") String Aligns,
			@QueryParam(value = "TAXON") String Taxon,
			@QueryParam(value = "TYPE") String Type) {
		List<String> listAligns = Arrays.asList(Aligns.split(","));
		return listAligns.stream().flatMap(align -> fastaDAO.getByAlign(align, Taxon, Type).stream()).collect(Collectors.toList());
	}	
	
	@GET
	@Produces("application/fasta")
	@Path("/type/{TYPE}")
	@UnitOfWork
	public List<Fasta> getFastaByType(
			@PathParam(value = "TYPE") String Types,
			@QueryParam(value = "TAXON") String Taxon,
			@QueryParam(value = "ALIGN") String Align) {
		List<String> listTypes = Arrays.asList(Types.split(","));
		return listTypes.stream().flatMap(typ -> fastaDAO.getByType(typ, Taxon, Align).stream()).collect(Collectors.toList());
	}	
	
	@GET
	@Produces("application/fasta")
	@Path("/UID/{UID}")
	@UnitOfWork
	public List<Fasta> getFastas(@PathParam(value = "UID") String UIDs) {
		List<Long> listUID = Arrays.asList(UIDs.split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
		List<Fasta> listFasta = listUID.stream().map(uid -> fastaDAO.getByUID(uid)).collect(Collectors.toList());
		return listFasta;
	}*/
	
	@GET
	@Produces("application/fasta")
	@Path("/DBD/Fasta/{TFCLASS}")
	@UnitOfWork
	public List<Fasta> getFasta(
			@PathParam(value = "TFCLASS") String TFClass,
			@QueryParam(value = "TAXON") String Taxon,
			@QueryParam(value = "VERSION") String Version){
		MetaFile file;
		List<MetaFile> result;
		if(Version == null) {
			result = metafileDAO.getNewestByTFClassID(TFClass, "Not_Aligned", "DBD");		
		}
		else {
			result = metafileDAO.getByTFClassID(TFClass, "Not_Aligned", "DBD", Long.parseLong(Version));
		}
		if(result.isEmpty()) {
			return new ArrayList<Fasta>();
		}
		file = result.get(0);
		if(file == null) {
			return new ArrayList<Fasta>();
		}
		return fastaDAO.getByFile(file,Taxon);
	}
	
	@GET
	@Produces("application/fasta")
	@Path("/DBD/Logoplot/{TFCLASS}")
	@UnitOfWork
	public List<Fasta> getFastaLogo(
			@PathParam(value = "TFCLASS") String TFClass,
			@QueryParam(value = "TAXON") String Taxon,
			@QueryParam(value = "VERSION") String Version){
		MetaFile file;
		List<MetaFile> result;
		if(Version == null) {
			result = metafileDAO.getNewestByTFClassID(TFClass, "Phyml", "DBD");		
		}
		else {
			result = metafileDAO.getByTFClassID(TFClass, "Phyml", "DBD", Long.parseLong(Version));
		}
		if(result.isEmpty()) {
			return new ArrayList<Fasta>();
		}
		file = result.get(0);
		if(file == null) {
			return new ArrayList<Fasta>();
		}
		return fastaDAO.getByFile(file,Taxon);
	}

	@GET
	@Produces("application/fasta")
	@Path("/DBD/Phyml/{TFCLASS}")
	@UnitOfWork
	public List<Fasta> getFastaPhyml(
			@PathParam(value = "TFCLASS") String TFClass,
			@QueryParam(value = "TAXON") String Taxon,
			@QueryParam(value = "VERSION") String Version){
		MetaFile file;
		List<MetaFile> result;
		if(Version == null) {
			result = metafileDAO.getNewestByTFClassID(TFClass, "Phyml", "DBD");		
		}
		else {
			result = metafileDAO.getByTFClassID(TFClass, "Phyml", "DBD", Long.parseLong(Version));
		}
		if(result.isEmpty()) {
			return new ArrayList<Fasta>();
		}
		file = result.get(0);
		if(file == null) {
			return new ArrayList<Fasta>();
		}
		return fastaDAO.getByFile(file,Taxon);
	}
	
	@GET
	@Produces("application/fasta")
	@Path("/DBD/Prank/{TFCLASS}")
	@UnitOfWork
	public List<Fasta> getFastaPrank(
			@PathParam(value = "TFCLASS") String TFClass,
			@QueryParam(value = "TAXON") String Taxon,
			@QueryParam(value = "VERSION") String Version){
		MetaFile file;
		List<MetaFile> result;
		if(Version == null) {
			result = metafileDAO.getNewestByTFClassID(TFClass, "Prank", "DBD");		
		}
		else {
			result = metafileDAO.getByTFClassID(TFClass, "Prank", "DBD", Long.parseLong(Version));
		}
		if(result.isEmpty()) {
			return new ArrayList<Fasta>();
		}
		file = result.get(0);
		if(file == null) {
			return new ArrayList<Fasta>();
		}
		return fastaDAO.getByFile(file,Taxon);
	}	
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/upload")
	@UnitOfWork
	public List<Fasta> addFastas(List<Fasta> listFasta) {
		System.out.println("Adding " + listFasta.size() + " entries");
		metafileDAO.create(listFasta.get(0).getFile());
		listFasta.forEach(fst -> fastaDAO.create(fst));
		return listFasta;
	}
}
