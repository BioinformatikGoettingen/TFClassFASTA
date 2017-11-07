package BioinformatikGoettingen.TFClassFASTA.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import BioinformatikGoettingen.TFClassFASTA.core.Fasta;
import BioinformatikGoettingen.TFClassFASTA.db.FastaDAO;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import io.dropwizard.setup.Environment;

@Path("/FASTA")
@Produces(MediaType.APPLICATION_JSON)
public class FastaResource {

	private final FastaDAO fastaDAO;
	private final Environment environment;
	
	public FastaResource(FastaDAO fastaDAO, Environment environment) {
		this.fastaDAO = fastaDAO;
		this.environment = environment;
	}
	
	@GET
	@Path("/{UID}")
	@UnitOfWork
	public Fasta getFasta(@PathParam(value = "UID") LongParam UID) {
		return fastaDAO.getByUID(UID.get());
	}
}
