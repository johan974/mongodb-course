package rc.legostore.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import rc.legostore.model.DeliveryInfo;
import rc.legostore.model.LegoSet;
import rc.legostore.model.LegoSetDifficulty;
import rc.legostore.model.ProductReview;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;


@Service
public class DbSeeder implements CommandLineRunner {
    private MongoTemplate legoSetRepository;
    @Autowired
    public DbSeeder(MongoTemplate legoSetRepository) {
        this.legoSetRepository = legoSetRepository;
    }
    @Override
    public void run(String... args) {
        legoSetRepository.dropCollection( LegoSet.class);

        LegoSet milleniumFalcon = new LegoSet(
                "Millennium Falcon",
                "Star Wars",
                LegoSetDifficulty.HARD,
                new DeliveryInfo(LocalDate.now().plusDays(1), 30, true),
                Arrays.asList(
                        new ProductReview("Dan", 7),
                        new ProductReview("Anna", 10),
                        new ProductReview("John", 8)
                )
        );
        LegoSet skyPolice = new LegoSet(
                "Sky Police Air Base",
                "City",
                LegoSetDifficulty.MEDIUM,
                new DeliveryInfo(LocalDate.now().plusDays(3), 50, true),
                Arrays.asList(
                        new ProductReview("Dan", 5),
                        new ProductReview("Andrew", 8)
                )
        );
        LegoSet mcLarenSenna = new LegoSet(
                "McLaren Senna",
                "Speed Champions",
                LegoSetDifficulty.EASY,
                new DeliveryInfo(LocalDate.now().plusDays(7), 70, false),
                Arrays.asList(
                        new ProductReview("Bogdan", 9),
                        new ProductReview("Christa", 9)
                )
        );
        LegoSet mindstormsEve = new LegoSet(
                "MINDSTORMS EV3",
                "Mindstorms",
                LegoSetDifficulty.HARD,
                new DeliveryInfo(LocalDate.now().plusDays(10), 100, false),
                Arrays.asList(
                        new ProductReview("Cosmin", 10),
                        new ProductReview("Jane", 9),
                        new ProductReview("James", 10)
                )
        );

        Collection<LegoSet> legoSets = Arrays.asList( milleniumFalcon, skyPolice, mcLarenSenna, mindstormsEve);
        legoSetRepository.insertAll( legoSets);
    }
}
