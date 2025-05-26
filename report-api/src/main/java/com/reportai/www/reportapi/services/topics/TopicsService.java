package com.reportai.www.reportapi.services.topics;

import com.reportai.www.reportapi.entities.School;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.Topic;
import com.reportai.www.reportapi.entities.attachments.TopicSubjectAttachment;
import com.reportai.www.reportapi.entities.helpers.Attachment;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.SubjectRepository;
import com.reportai.www.reportapi.repositories.TopicRepository;
import com.reportai.www.reportapi.repositories.TopicSubjectAttachmentRepository;
import com.reportai.www.reportapi.services.common.ISimpleCreate;
import com.reportai.www.reportapi.services.common.ISimpleRead;
import com.reportai.www.reportapi.services.levels.LevelsService;
import com.reportai.www.reportapi.services.subjects.SubjectsService;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class TopicsService implements ISimpleRead<Topic>, ISimpleCreate<Topic> {
    public enum TOPICS {
        // English Language Topics
        GRAMMAR_BASICS("Grammar Basics", SubjectsService.SUBJECTS.ENGLISH),
        COMPREHENSION("Reading Comprehension", SubjectsService.SUBJECTS.ENGLISH),
        COMPOSITION_WRITING("Composition Writing", SubjectsService.SUBJECTS.ENGLISH),
        VOCABULARY_BUILDING("Vocabulary Building", SubjectsService.SUBJECTS.ENGLISH),
        ORAL_COMMUNICATION("Oral Communication", SubjectsService.SUBJECTS.ENGLISH),
        LISTENING_COMPREHENSION("Listening Comprehension", SubjectsService.SUBJECTS.ENGLISH),
        POETRY_ANALYSIS("Poetry Analysis", SubjectsService.SUBJECTS.ENGLISH, SubjectsService.SUBJECTS.LITERATURE_ENGLISH, SubjectsService.SUBJECTS.LITERATURE_ENGLISH, SubjectsService.SUBJECTS.LITERATURE_H1, SubjectsService.SUBJECTS.LITERATURE_H2),
        NARRATIVE_WRITING("Narrative Writing", SubjectsService.SUBJECTS.ENGLISH),
        ARGUMENTATIVE_WRITING("Argumentative Writing", SubjectsService.SUBJECTS.ENGLISH),

        // Mathematics Primary Topics
        NUMBERS_AND_OPERATIONS("Numbers and Operations", SubjectsService.SUBJECTS.MATHEMATICS),
        ADDITION_SUBTRACTION("Addition and Subtraction", SubjectsService.SUBJECTS.MATHEMATICS),
        MULTIPLICATION_DIVISION("Multiplication and Division", SubjectsService.SUBJECTS.MATHEMATICS),
        FRACTIONS("Fractions", SubjectsService.SUBJECTS.MATHEMATICS),
        DECIMALS("Decimals", SubjectsService.SUBJECTS.MATHEMATICS),
        GEOMETRY_SHAPES("Geometry and Shapes", SubjectsService.SUBJECTS.MATHEMATICS),
        MEASUREMENT("Measurement", SubjectsService.SUBJECTS.MATHEMATICS),
        DATA_HANDLING("Data Handling", SubjectsService.SUBJECTS.MATHEMATICS),
        MONEY_CONCEPTS("Money Concepts", SubjectsService.SUBJECTS.MATHEMATICS),
        TIME_CONCEPTS("Time Concepts", SubjectsService.SUBJECTS.MATHEMATICS),

        // Mathematics Secondary Topics
        ALGEBRA_BASICS("Algebraic Expressions", SubjectsService.SUBJECTS.MATHEMATICS),
        LINEAR_EQUATIONS("Linear Equations", SubjectsService.SUBJECTS.MATHEMATICS),
        SIMULTANEOUS_EQUATIONS("Simultaneous Equations", SubjectsService.SUBJECTS.MATHEMATICS),
        QUADRATIC_EQUATIONS("Quadratic Equations", SubjectsService.SUBJECTS.MATHEMATICS),
        INDICES_SURDS("Indices and Surds", SubjectsService.SUBJECTS.MATHEMATICS),
        POLYNOMIALS("Polynomials", SubjectsService.SUBJECTS.MATHEMATICS),
        COORDINATE_GEOMETRY("Coordinate Geometry", SubjectsService.SUBJECTS.MATHEMATICS),
        TRIGONOMETRY_BASICS("Trigonometry", SubjectsService.SUBJECTS.MATHEMATICS),
        GEOMETRY_CIRCLES("Circle Properties", SubjectsService.SUBJECTS.MATHEMATICS),
        SIMILARITY_CONGRUENCY("Similarity and Congruency", SubjectsService.SUBJECTS.MATHEMATICS),
        STATISTICS_PROBABILITY("Statistics and Probability", SubjectsService.SUBJECTS.MATHEMATICS),
        MENSURATION("Mensuration", SubjectsService.SUBJECTS.MATHEMATICS),

        // Additional Mathematics Topics
        FUNCTIONS("Functions", SubjectsService.SUBJECTS.ADDITIONAL_MATHEMATICS, SubjectsService.SUBJECTS.MATHEMATICS_H2),
        DIFFERENTIATION("Differentiation", SubjectsService.SUBJECTS.ADDITIONAL_MATHEMATICS),
        INTEGRATION("Integration", SubjectsService.SUBJECTS.ADDITIONAL_MATHEMATICS),
        EXPONENTIAL_LOGARITHMIC("Exponential and Logarithmic Functions", SubjectsService.SUBJECTS.ADDITIONAL_MATHEMATICS),
        TRIGONOMETRIC_FUNCTIONS("Trigonometric Functions", SubjectsService.SUBJECTS.ADDITIONAL_MATHEMATICS),
        TRIGONOMETRIC_IDENTITIES("Trigonometric Identities", SubjectsService.SUBJECTS.ADDITIONAL_MATHEMATICS),
        BINOMIAL_THEOREM("Binomial Theorem", SubjectsService.SUBJECTS.ADDITIONAL_MATHEMATICS),
        PARTIAL_FRACTIONS("Partial Fractions", SubjectsService.SUBJECTS.ADDITIONAL_MATHEMATICS),
        SEQUENCES_SERIES("Sequences and Series", SubjectsService.SUBJECTS.ADDITIONAL_MATHEMATICS, SubjectsService.SUBJECTS.MATHEMATICS_H2),
        COORDINATE_GEOMETRY_ADV("Advanced Coordinate Geometry", SubjectsService.SUBJECTS.ADDITIONAL_MATHEMATICS),
        VECTORS_2D("Vectors in 2D", SubjectsService.SUBJECTS.ADDITIONAL_MATHEMATICS),

        // Physics Topics
        MEASUREMENT_PHYSICS("Measurement and Units", SubjectsService.SUBJECTS.PHYSICS, SubjectsService.SUBJECTS.PHYSICS_H1, SubjectsService.SUBJECTS.PHYSICS_H2),
        MECHANICS_KINEMATICS("Kinematics", SubjectsService.SUBJECTS.PHYSICS, SubjectsService.SUBJECTS.PHYSICS_H1, SubjectsService.SUBJECTS.PHYSICS_H2),
        MECHANICS_DYNAMICS("Dynamics", SubjectsService.SUBJECTS.PHYSICS, SubjectsService.SUBJECTS.PHYSICS_H1, SubjectsService.SUBJECTS.PHYSICS_H2),
        FORCES_MOTION("Forces and Motion", SubjectsService.SUBJECTS.PHYSICS, SubjectsService.SUBJECTS.PHYSICS_H1, SubjectsService.SUBJECTS.PHYSICS_H2),
        WORK_ENERGY_POWER("Work, Energy and Power", SubjectsService.SUBJECTS.PHYSICS, SubjectsService.SUBJECTS.PHYSICS_H1, SubjectsService.SUBJECTS.PHYSICS_H2),
        MOMENTUM("Momentum", SubjectsService.SUBJECTS.PHYSICS, SubjectsService.SUBJECTS.PHYSICS_H1, SubjectsService.SUBJECTS.PHYSICS_H2),
        CIRCULAR_MOTION("Circular Motion", SubjectsService.SUBJECTS.PHYSICS_H2),
        GRAVITATIONAL_FIELD("Gravitational Field", SubjectsService.SUBJECTS.PHYSICS_H2),
        OSCILLATIONS("Oscillations", SubjectsService.SUBJECTS.PHYSICS_H2),
        WAVES("Wave Motion", SubjectsService.SUBJECTS.PHYSICS, SubjectsService.SUBJECTS.PHYSICS_H1, SubjectsService.SUBJECTS.PHYSICS_H2),
        SUPERPOSITION("Superposition", SubjectsService.SUBJECTS.PHYSICS_H2),
        THERMAL_PHYSICS("Thermal Physics", SubjectsService.SUBJECTS.PHYSICS, SubjectsService.SUBJECTS.PHYSICS_H1, SubjectsService.SUBJECTS.PHYSICS_H2),
        ELECTRICITY_MAGNETISM("Electricity and Magnetism", SubjectsService.SUBJECTS.PHYSICS, SubjectsService.SUBJECTS.PHYSICS_H1, SubjectsService.SUBJECTS.PHYSICS_H2),
        ELECTROMAGNETIC_INDUCTION("Electromagnetic Induction", SubjectsService.SUBJECTS.PHYSICS_H2),
        ALTERNATING_CURRENT("Alternating Current", SubjectsService.SUBJECTS.PHYSICS_H2),
        QUANTUM_PHYSICS("Quantum Physics", SubjectsService.SUBJECTS.PHYSICS_H2),
        ATOMIC_NUCLEAR_PHYSICS("Atomic and Nuclear Physics", SubjectsService.SUBJECTS.PHYSICS_H2),

        // Chemistry Topics
        ATOMIC_STRUCTURE("Atomic Structure", SubjectsService.SUBJECTS.CHEMISTRY, SubjectsService.SUBJECTS.CHEMISTRY_H1, SubjectsService.SUBJECTS.CHEMISTRY_H2),
        CHEMICAL_BONDING("Chemical Bonding", SubjectsService.SUBJECTS.CHEMISTRY, SubjectsService.SUBJECTS.CHEMISTRY_H1, SubjectsService.SUBJECTS.CHEMISTRY_H2),
        STOICHIOMETRY("Stoichiometry", SubjectsService.SUBJECTS.CHEMISTRY, SubjectsService.SUBJECTS.CHEMISTRY_H1, SubjectsService.SUBJECTS.CHEMISTRY_H2),
        STATES_OF_MATTER("States of Matter", SubjectsService.SUBJECTS.CHEMISTRY, SubjectsService.SUBJECTS.CHEMISTRY_H1, SubjectsService.SUBJECTS.CHEMISTRY_H2),
        ACIDS_BASES_SALTS("Acids, Bases and Salts", SubjectsService.SUBJECTS.CHEMISTRY, SubjectsService.SUBJECTS.CHEMISTRY_H1, SubjectsService.SUBJECTS.CHEMISTRY_H2),
        REDOX_REACTIONS("Redox reactions", SubjectsService.SUBJECTS.CHEMISTRY, SubjectsService.SUBJECTS.CHEMISTRY_H1, SubjectsService.SUBJECTS.CHEMISTRY_H2),
        ORGANIC_CHEMISTRY("Organic Chemistry", SubjectsService.SUBJECTS.CHEMISTRY, SubjectsService.SUBJECTS.CHEMISTRY_H1, SubjectsService.SUBJECTS.CHEMISTRY_H2),
        CHEMICAL_KINETICS("Chemical Kinetics", SubjectsService.SUBJECTS.CHEMISTRY_H2),
        CHEMICAL_EQUILIBRIUM("Chemical Equilibrium", SubjectsService.SUBJECTS.CHEMISTRY_H2),
        THERMOCHEMISTRY("Thermochemistry", SubjectsService.SUBJECTS.CHEMISTRY_H2),
        ELECTROCHEMISTRY("Electrochemistry", SubjectsService.SUBJECTS.CHEMISTRY_H2),
        TRANSITION_ELEMENTS("Transition Elements", SubjectsService.SUBJECTS.CHEMISTRY_H2),
        ISOMERISM("Isomerism", SubjectsService.SUBJECTS.CHEMISTRY_H2),
        POLYMERS("Polymers", SubjectsService.SUBJECTS.CHEMISTRY_H2),

        // Biology Topics
        CELL_STRUCTURE_FUNCTION("Cell Structure and Function", SubjectsService.SUBJECTS.BIOLOGY, SubjectsService.SUBJECTS.BIOLOGY_H1, SubjectsService.SUBJECTS.BIOLOGY_H2),
        BIOLOGICAL_MOLECULES("Biological Molecules", SubjectsService.SUBJECTS.BIOLOGY, SubjectsService.SUBJECTS.BIOLOGY_H1, SubjectsService.SUBJECTS.BIOLOGY_H2),
        ENZYMES("Enzymes", SubjectsService.SUBJECTS.BIOLOGY, SubjectsService.SUBJECTS.BIOLOGY_H1, SubjectsService.SUBJECTS.BIOLOGY_H2),
        CELL_MEMBRANE_TRANSPORT("Cell Membrane and Transport", SubjectsService.SUBJECTS.BIOLOGY, SubjectsService.SUBJECTS.BIOLOGY_H1, SubjectsService.SUBJECTS.BIOLOGY_H2),
        CELLULAR_RESPIRATION("Cellular Respiration", SubjectsService.SUBJECTS.BIOLOGY, SubjectsService.SUBJECTS.BIOLOGY_H1, SubjectsService.SUBJECTS.BIOLOGY_H2),
        PHOTOSYNTHESIS("Photosynthesis", SubjectsService.SUBJECTS.BIOLOGY, SubjectsService.SUBJECTS.BIOLOGY_H1, SubjectsService.SUBJECTS.BIOLOGY_H2),
        MITOSIS_MEIOSIS("Mitosis and Meiosis", SubjectsService.SUBJECTS.BIOLOGY, SubjectsService.SUBJECTS.BIOLOGY_H1, SubjectsService.SUBJECTS.BIOLOGY_H2),
        GENETICS("Genetics", SubjectsService.SUBJECTS.BIOLOGY, SubjectsService.SUBJECTS.BIOLOGY_H1, SubjectsService.SUBJECTS.BIOLOGY_H2),
        MOLECULAR_GENETICS("Molecular Genetics", SubjectsService.SUBJECTS.BIOLOGY_H2),
        HOMEOSTASIS("Homeostasis", SubjectsService.SUBJECTS.BIOLOGY, SubjectsService.SUBJECTS.BIOLOGY_H1, SubjectsService.SUBJECTS.BIOLOGY_H2),
        COORDINATION_RESPONSE("Coordination and Response", SubjectsService.SUBJECTS.BIOLOGY, SubjectsService.SUBJECTS.BIOLOGY_H1, SubjectsService.SUBJECTS.BIOLOGY_H2),
        NUTRITION("Nutrition", SubjectsService.SUBJECTS.BIOLOGY, SubjectsService.SUBJECTS.BIOLOGY_H1, SubjectsService.SUBJECTS.BIOLOGY_H2),
        TRANSPORT_ORGANISMS("Transport in Organisms", SubjectsService.SUBJECTS.BIOLOGY, SubjectsService.SUBJECTS.BIOLOGY_H1, SubjectsService.SUBJECTS.BIOLOGY_H2),
        REPRODUCTION("Reproduction", SubjectsService.SUBJECTS.BIOLOGY, SubjectsService.SUBJECTS.BIOLOGY_H1, SubjectsService.SUBJECTS.BIOLOGY_H2),
        ECOLOGY("Ecology", SubjectsService.SUBJECTS.BIOLOGY, SubjectsService.SUBJECTS.BIOLOGY_H1, SubjectsService.SUBJECTS.BIOLOGY_H2),
        EVOLUTION("Evolution", SubjectsService.SUBJECTS.BIOLOGY_H2),
        APPLICATIONS_BIOLOGY("Applications of Biology", SubjectsService.SUBJECTS.BIOLOGY_H2),

        // Science Primary Topics
        LIVING_NON_LIVING("Living and Non-living Things", SubjectsService.SUBJECTS.SCIENCE),
        PLANTS_ANIMALS("Plants and Animals", SubjectsService.SUBJECTS.SCIENCE),
        HUMAN_BODY_SYSTEMS("Human Body Systems", SubjectsService.SUBJECTS.SCIENCE),
        MATERIALS_PROPERTIES("Materials and Properties", SubjectsService.SUBJECTS.SCIENCE),
        MAGNETS("Magnets", SubjectsService.SUBJECTS.SCIENCE),
        LIGHT_SHADOWS("Light and Shadows", SubjectsService.SUBJECTS.SCIENCE),
        HEAT_TEMPERATURE("Heat and Temperature", SubjectsService.SUBJECTS.SCIENCE),
        WATER_CYCLE("Water Cycle", SubjectsService.SUBJECTS.SCIENCE),

        // History Topics
        ANCIENT_CIVILIZATIONS("Ancient Civilizations", SubjectsService.SUBJECTS.HISTORY, SubjectsService.SUBJECTS.HISTORY_H1, SubjectsService.SUBJECTS.HISTORY_H2),
        COLONIAL_SINGAPORE("Colonial Singapore", SubjectsService.SUBJECTS.HISTORY, SubjectsService.SUBJECTS.HISTORY_H1, SubjectsService.SUBJECTS.HISTORY_H2),
        WORLD_WAR_TWO("World War II", SubjectsService.SUBJECTS.HISTORY, SubjectsService.SUBJECTS.HISTORY_H1, SubjectsService.SUBJECTS.HISTORY_H2),
        INDEPENDENCE_SINGAPORE("Singapore's Independence", SubjectsService.SUBJECTS.HISTORY, SubjectsService.SUBJECTS.HISTORY_H1, SubjectsService.SUBJECTS.HISTORY_H2),
        COLD_WAR("Cold War", SubjectsService.SUBJECTS.HISTORY_H2),
        SOUTHEAST_ASIAN_HISTORY("Southeast Asian History", SubjectsService.SUBJECTS.HISTORY, SubjectsService.SUBJECTS.HISTORY_H1, SubjectsService.SUBJECTS.HISTORY_H2),
        NATIONALISM_SOUTHEAST_ASIA("Nationalism in Southeast Asia", SubjectsService.SUBJECTS.HISTORY_H2),

        // Geography Topics
        PHYSICAL_GEOGRAPHY("Physical Geography", SubjectsService.SUBJECTS.GEOGRAPHY, SubjectsService.SUBJECTS.GEOGRAPHY_H1, SubjectsService.SUBJECTS.GEOGRAPHY_H2),
        WEATHER_CLIMATE_GEO("Weather and Climate", SubjectsService.SUBJECTS.SCIENCE, SubjectsService.SUBJECTS.GEOGRAPHY, SubjectsService.SUBJECTS.GEOGRAPHY_H1, SubjectsService.SUBJECTS.GEOGRAPHY_H2),
        PLATE_TECTONICS("Plate Tectonics", SubjectsService.SUBJECTS.GEOGRAPHY, SubjectsService.SUBJECTS.GEOGRAPHY_H1, SubjectsService.SUBJECTS.GEOGRAPHY_H2),
        POPULATION_STUDIES("Population Studies", SubjectsService.SUBJECTS.GEOGRAPHY, SubjectsService.SUBJECTS.GEOGRAPHY_H1, SubjectsService.SUBJECTS.GEOGRAPHY_H2),
        SETTLEMENT_GEOGRAPHY("Settlement Geography", SubjectsService.SUBJECTS.GEOGRAPHY, SubjectsService.SUBJECTS.GEOGRAPHY_H1, SubjectsService.SUBJECTS.GEOGRAPHY_H2),
        ECONOMIC_GEOGRAPHY("Economic Geography", SubjectsService.SUBJECTS.GEOGRAPHY, SubjectsService.SUBJECTS.GEOGRAPHY_H1, SubjectsService.SUBJECTS.GEOGRAPHY_H2),
        ENVIRONMENTAL_GEOGRAPHY("Environmental Geography", SubjectsService.SUBJECTS.GEOGRAPHY, SubjectsService.SUBJECTS.GEOGRAPHY_H1, SubjectsService.SUBJECTS.GEOGRAPHY_H2),
        DEVELOPMENT_GEOGRAPHY("Development Geography", SubjectsService.SUBJECTS.GEOGRAPHY_H2),
        TOURISM_GEOGRAPHY("Tourism Geography", SubjectsService.SUBJECTS.GEOGRAPHY_H2),

        // Economics Topics
        BASIC_ECONOMIC_PROBLEM("Basic Economic Problem", SubjectsService.SUBJECTS.ECONOMICS, SubjectsService.SUBJECTS.ECONOMICS_H1, SubjectsService.SUBJECTS.ECONOMICS_H2),
        DEMAND_SUPPLY("Demand and Supply", SubjectsService.SUBJECTS.ECONOMICS, SubjectsService.SUBJECTS.ECONOMICS_H1, SubjectsService.SUBJECTS.ECONOMICS_H2),
        MARKET_STRUCTURES("Market Structures", SubjectsService.SUBJECTS.ECONOMICS, SubjectsService.SUBJECTS.ECONOMICS_H1, SubjectsService.SUBJECTS.ECONOMICS_H2),
        MARKET_FAILURE("Market Failure", SubjectsService.SUBJECTS.ECONOMICS, SubjectsService.SUBJECTS.ECONOMICS_H1, SubjectsService.SUBJECTS.ECONOMICS_H2),
        MACROECONOMICS("Macroeconomics", SubjectsService.SUBJECTS.ECONOMICS, SubjectsService.SUBJECTS.ECONOMICS_H1, SubjectsService.SUBJECTS.ECONOMICS_H2),
        INTERNATIONAL_TRADE("International Trade", SubjectsService.SUBJECTS.ECONOMICS, SubjectsService.SUBJECTS.ECONOMICS_H1, SubjectsService.SUBJECTS.ECONOMICS_H2),
        ECONOMIC_DEVELOPMENT("Economic Development", SubjectsService.SUBJECTS.ECONOMICS_H2),
        MONETARY_POLICY("Monetary Policy", SubjectsService.SUBJECTS.ECONOMICS_H2),
        FISCAL_POLICY("Fiscal Policy", SubjectsService.SUBJECTS.ECONOMICS_H2),

        // H2 Mathematics Topics
        VECTORS_3D("Vectors", SubjectsService.SUBJECTS.MATHEMATICS_H2),
        COMPLEX_NUMBERS("Complex Numbers", SubjectsService.SUBJECTS.MATHEMATICS_H2),
        CALCULUS_DIFFERENTIATION("Calculus - Differentiation", SubjectsService.SUBJECTS.MATHEMATICS_H2),
        CALCULUS_INTEGRATION("Calculus - Integration", SubjectsService.SUBJECTS.MATHEMATICS_H2),
        DIFFERENTIAL_EQUATIONS("Differential Equations", SubjectsService.SUBJECTS.MATHEMATICS_H2),
        MACLAURIN_SERIES("Maclaurin Series", SubjectsService.SUBJECTS.MATHEMATICS_H2),
        PROBABILITY_STATISTICS_H2("Probability and Statistics", SubjectsService.SUBJECTS.MATHEMATICS_H2),
        NORMAL_DISTRIBUTION("Normal Distribution", SubjectsService.SUBJECTS.MATHEMATICS_H2),
        HYPOTHESIS_TESTING("Hypothesis Testing", SubjectsService.SUBJECTS.MATHEMATICS_H2),
        CORRELATION_REGRESSION("Correlation and Regression", SubjectsService.SUBJECTS.MATHEMATICS_H2),

        // Chinese Language Topics
        CHINESE_COMPOSITION("Chinese Composition", SubjectsService.SUBJECTS.CHINESE, SubjectsService.SUBJECTS.HIGHER_CHINESE),
        CHINESE_COMPREHENSION("Chinese Comprehension", SubjectsService.SUBJECTS.CHINESE, SubjectsService.SUBJECTS.HIGHER_CHINESE),
        CHINESE_ORAL("Chinese Oral Communication", SubjectsService.SUBJECTS.CHINESE, SubjectsService.SUBJECTS.HIGHER_CHINESE),
        CHINESE_LISTENING("Chinese Listening Comprehension", SubjectsService.SUBJECTS.CHINESE, SubjectsService.SUBJECTS.HIGHER_CHINESE),
        CHINESE_GRAMMAR("Chinese Grammar", SubjectsService.SUBJECTS.CHINESE, SubjectsService.SUBJECTS.HIGHER_CHINESE),
        CHINESE_VOCABULARY("Chinese Vocabulary", SubjectsService.SUBJECTS.CHINESE, SubjectsService.SUBJECTS.HIGHER_CHINESE),
        CHINESE_POETRY("Chinese Poetry", SubjectsService.SUBJECTS.HIGHER_CHINESE, SubjectsService.SUBJECTS.LITERATURE_CHINESE),
        CHINESE_CLASSICAL_TEXTS("Chinese Classical Texts", SubjectsService.SUBJECTS.HIGHER_CHINESE, SubjectsService.SUBJECTS.LITERATURE_CHINESE),

        // Malay Language Topics
        MALAY_COMPOSITION("Malay Composition", SubjectsService.SUBJECTS.MALAY, SubjectsService.SUBJECTS.MALAY, SubjectsService.SUBJECTS.HIGHER_MALAY),
        MALAY_COMPREHENSION("Malay Comprehension", SubjectsService.SUBJECTS.MALAY, SubjectsService.SUBJECTS.MALAY, SubjectsService.SUBJECTS.HIGHER_MALAY),
        MALAY_ORAL("Malay Oral Communication", SubjectsService.SUBJECTS.MALAY, SubjectsService.SUBJECTS.MALAY, SubjectsService.SUBJECTS.HIGHER_MALAY),
        MALAY_LISTENING("Malay Listening Comprehension", SubjectsService.SUBJECTS.MALAY, SubjectsService.SUBJECTS.MALAY, SubjectsService.SUBJECTS.HIGHER_MALAY),
        MALAY_GRAMMAR("Malay Grammar", SubjectsService.SUBJECTS.MALAY, SubjectsService.SUBJECTS.MALAY, SubjectsService.SUBJECTS.HIGHER_MALAY),
        MALAY_LITERATURE("Malay Literature", SubjectsService.SUBJECTS.HIGHER_MALAY, SubjectsService.SUBJECTS.LITERATURE_MALAY),

        // Tamil Language Topics
        TAMIL_COMPOSITION("Tamil Composition", SubjectsService.SUBJECTS.TAMIL, SubjectsService.SUBJECTS.TAMIL, SubjectsService.SUBJECTS.HIGHER_TAMIL),
        TAMIL_COMPREHENSION("Tamil Comprehension", SubjectsService.SUBJECTS.TAMIL, SubjectsService.SUBJECTS.TAMIL, SubjectsService.SUBJECTS.HIGHER_TAMIL),
        TAMIL_ORAL("Tamil Oral Communication", SubjectsService.SUBJECTS.TAMIL, SubjectsService.SUBJECTS.TAMIL, SubjectsService.SUBJECTS.HIGHER_TAMIL),
        TAMIL_LISTENING("Tamil Listening Comprehension", SubjectsService.SUBJECTS.TAMIL, SubjectsService.SUBJECTS.TAMIL, SubjectsService.SUBJECTS.HIGHER_TAMIL),
        TAMIL_GRAMMAR("Tamil Grammar", SubjectsService.SUBJECTS.TAMIL, SubjectsService.SUBJECTS.TAMIL, SubjectsService.SUBJECTS.HIGHER_TAMIL),
        TAMIL_LITERATURE("Tamil Literature", SubjectsService.SUBJECTS.HIGHER_TAMIL, SubjectsService.SUBJECTS.LITERATURE_TAMIL),

        // Literature in English Topics
        PROSE_ANALYSIS("Prose Analysis", SubjectsService.SUBJECTS.LITERATURE_ENGLISH, SubjectsService.SUBJECTS.LITERATURE_H1, SubjectsService.SUBJECTS.LITERATURE_H2),
        DRAMA_ANALYSIS("Drama Analysis", SubjectsService.SUBJECTS.LITERATURE_ENGLISH, SubjectsService.SUBJECTS.LITERATURE_H1, SubjectsService.SUBJECTS.LITERATURE_H2),
        LITERARY_DEVICES("Literary Devices", SubjectsService.SUBJECTS.LITERATURE_ENGLISH, SubjectsService.SUBJECTS.LITERATURE_H1, SubjectsService.SUBJECTS.LITERATURE_H2),
        THEMES_MOTIFS("Themes and Motifs", SubjectsService.SUBJECTS.LITERATURE_ENGLISH, SubjectsService.SUBJECTS.LITERATURE_H1, SubjectsService.SUBJECTS.LITERATURE_H2),
        CHARACTER_ANALYSIS("Character Analysis", SubjectsService.SUBJECTS.LITERATURE_ENGLISH, SubjectsService.SUBJECTS.LITERATURE_H1, SubjectsService.SUBJECTS.LITERATURE_H2),
        COMPARATIVE_LITERATURE("Comparative Literature", SubjectsService.SUBJECTS.LITERATURE_H2),
        CREATIVE_WRITING_LIT("Creative Writing", SubjectsService.SUBJECTS.LITERATURE_H2),

        // Social Studies Topics
        SINGAPORE_SOCIETY("Singapore Society", SubjectsService.SUBJECTS.SOCIAL_STUDIES),
        CULTURAL_DIVERSITY("Cultural Diversity", SubjectsService.SUBJECTS.SOCIAL_STUDIES),
        GOVERNANCE_SINGAPORE("Governance in Singapore", SubjectsService.SUBJECTS.SOCIAL_STUDIES),
        CITIZENSHIP("Citizenship", SubjectsService.SUBJECTS.SOCIAL_STUDIES),
        GLOBALISATION("Globalisation", SubjectsService.SUBJECTS.SOCIAL_STUDIES),
        TOURISM_SINGAPORE("Tourism in Singapore", SubjectsService.SUBJECTS.SOCIAL_STUDIES),
        HOUSING_SINGAPORE("Housing in Singapore", SubjectsService.SUBJECTS.SOCIAL_STUDIES),
        TRANSPORT_SINGAPORE("Transport in Singapore", SubjectsService.SUBJECTS.SOCIAL_STUDIES),

        // Design and Technology Topics
        DESIGN_PROCESS("Design Process", SubjectsService.SUBJECTS.DESIGN_AND_TECHNOLOGY),
        MATERIALS_TECHNOLOGY("Materials and Technology", SubjectsService.SUBJECTS.DESIGN_AND_TECHNOLOGY),
        ENGINEERING_DRAWING("Engineering Drawing", SubjectsService.SUBJECTS.DESIGN_AND_TECHNOLOGY),
        WOODWORKING("Woodworking", SubjectsService.SUBJECTS.DESIGN_AND_TECHNOLOGY),
        METALWORKING("Metalworking", SubjectsService.SUBJECTS.DESIGN_AND_TECHNOLOGY),
        ELECTRONICS_BASICS("Electronics Basics", SubjectsService.SUBJECTS.DESIGN_AND_TECHNOLOGY),
        MECHANISMS("Mechanisms", SubjectsService.SUBJECTS.DESIGN_AND_TECHNOLOGY),
        SUSTAINABLE_DESIGN("Sustainable Design", SubjectsService.SUBJECTS.DESIGN_AND_TECHNOLOGY),

        // Food and Nutrition Topics
        NUTRITIONAL_SCIENCE("Nutritional Science", SubjectsService.SUBJECTS.FOOD_AND_NUTRITION),
        FOOD_SAFETY_HYGIENE("Food Safety and Hygiene", SubjectsService.SUBJECTS.FOOD_AND_NUTRITION),
        FOOD_PREPARATION("Food Preparation Techniques", SubjectsService.SUBJECTS.FOOD_AND_NUTRITION),
        MEAL_PLANNING("Meal Planning", SubjectsService.SUBJECTS.FOOD_AND_NUTRITION),
        DIETARY_REQUIREMENTS("Dietary Requirements", SubjectsService.SUBJECTS.FOOD_AND_NUTRITION),
        FOOD_PRESERVATION("Food Preservation", SubjectsService.SUBJECTS.FOOD_AND_NUTRITION),
        CULINARY_SKILLS("Culinary Skills", SubjectsService.SUBJECTS.FOOD_AND_NUTRITION),

        // Computing Topics
        COMPUTATIONAL_THINKING("Computational Thinking", SubjectsService.SUBJECTS.COMPUTING, SubjectsService.SUBJECTS.COMPUTING_H2, SubjectsService.SUBJECTS.COMPUTER_APPLICATIONS),
        PROGRAMMING_FUNDAMENTALS("Programming Fundamentals", SubjectsService.SUBJECTS.COMPUTING, SubjectsService.SUBJECTS.COMPUTING_H2),
        ALGORITHMS_DATA_STRUCTURES("Algorithms and Data Structures", SubjectsService.SUBJECTS.COMPUTING, SubjectsService.SUBJECTS.COMPUTING_H2),
        SOFTWARE_ENGINEERING("Software Engineering", SubjectsService.SUBJECTS.COMPUTING_H2),
        DATABASE_SYSTEMS("Database Systems", SubjectsService.SUBJECTS.COMPUTING_H2),
        COMPUTER_NETWORKS("Computer Networks", SubjectsService.SUBJECTS.COMPUTING_H2),
        CYBERSECURITY("Cybersecurity", SubjectsService.SUBJECTS.COMPUTING_H2),
        ARTIFICIAL_INTELLIGENCE("Artificial Intelligence", SubjectsService.SUBJECTS.COMPUTING_H2),
        WEB_DEVELOPMENT("Web Development", SubjectsService.SUBJECTS.COMPUTER_APPLICATIONS, SubjectsService.SUBJECTS.COMPUTING),
        SPREADSHEET_APPLICATIONS("Spreadsheet Applications", SubjectsService.SUBJECTS.COMPUTER_APPLICATIONS),
        PRESENTATION_SOFTWARE("Presentation Software", SubjectsService.SUBJECTS.COMPUTER_APPLICATIONS),

        // Art Topics
        DRAWING_TECHNIQUES("Drawing Techniques", SubjectsService.SUBJECTS.ART, SubjectsService.SUBJECTS.ART_H2),
        PAINTING_TECHNIQUES("Painting Techniques", SubjectsService.SUBJECTS.ART, SubjectsService.SUBJECTS.ART_H2),
        SCULPTURE("Sculpture", SubjectsService.SUBJECTS.ART, SubjectsService.SUBJECTS.ART_H2),
        PRINTMAKING("Printmaking", SubjectsService.SUBJECTS.ART, SubjectsService.SUBJECTS.ART_H2),
        DIGITAL_ART("Digital Art", SubjectsService.SUBJECTS.ART, SubjectsService.SUBJECTS.ART_H2),
        ART_HISTORY("Art History", SubjectsService.SUBJECTS.ART, SubjectsService.SUBJECTS.ART_H2),
        ART_APPRECIATION("Art Appreciation", SubjectsService.SUBJECTS.ART, SubjectsService.SUBJECTS.ART_H2),
        MIXED_MEDIA("Mixed Media", SubjectsService.SUBJECTS.ART, SubjectsService.SUBJECTS.ART_H2),

        // Music Topics
        MUSIC_THEORY("Music Theory", SubjectsService.SUBJECTS.MUSIC, SubjectsService.SUBJECTS.MUSIC_H2),
        MUSIC_COMPOSITION("Music Composition", SubjectsService.SUBJECTS.MUSIC, SubjectsService.SUBJECTS.MUSIC_H2),
        MUSIC_PERFORMANCE("Music Performance", SubjectsService.SUBJECTS.MUSIC, SubjectsService.SUBJECTS.MUSIC_H2),
        MUSIC_HISTORY("Music History", SubjectsService.SUBJECTS.MUSIC, SubjectsService.SUBJECTS.MUSIC_H2),
        MUSIC_APPRECIATION("Music Appreciation", SubjectsService.SUBJECTS.MUSIC, SubjectsService.SUBJECTS.MUSIC_H2),
        INSTRUMENTAL_TECHNIQUES("Instrumental Techniques", SubjectsService.SUBJECTS.MUSIC, SubjectsService.SUBJECTS.MUSIC_H2),
        VOCAL_TECHNIQUES("Vocal Techniques", SubjectsService.SUBJECTS.MUSIC, SubjectsService.SUBJECTS.MUSIC_H2),
        MUSIC_TECHNOLOGY("Music Technology", SubjectsService.SUBJECTS.MUSIC_H2),

        // Physical Education Topics
        FUNDAMENTAL_MOVEMENT("Fundamental Movement Skills", SubjectsService.SUBJECTS.PHYSICAL_EDUCATION),
        TEAM_SPORTS("Team Sports", SubjectsService.SUBJECTS.PHYSICAL_EDUCATION),
        INDIVIDUAL_SPORTS("Individual Sports", SubjectsService.SUBJECTS.PHYSICAL_EDUCATION),
        FITNESS_CONDITIONING("Fitness and Conditioning", SubjectsService.SUBJECTS.PHYSICAL_EDUCATION),
        OUTDOOR_EDUCATION("Outdoor Education", SubjectsService.SUBJECTS.PHYSICAL_EDUCATION),
        SPORTS_SCIENCE("Sports Science", SubjectsService.SUBJECTS.PHYSICAL_EDUCATION),
        HEALTHY_LIFESTYLE("Healthy Lifestyle", SubjectsService.SUBJECTS.PHYSICAL_EDUCATION),
        SAFETY_RISK_MANAGEMENT("Safety and Risk Management", SubjectsService.SUBJECTS.PHYSICAL_EDUCATION),

        // Business Studies Topics
        BUSINESS_ENVIRONMENT("Business Environment", SubjectsService.SUBJECTS.BUSINESS_STUDIES),
        BUSINESS_PLANNING("Business Planning", SubjectsService.SUBJECTS.BUSINESS_STUDIES),
        MARKETING_FUNDAMENTALS("Marketing Fundamentals", SubjectsService.SUBJECTS.BUSINESS_STUDIES),
        HUMAN_RESOURCES("Human Resource Management", SubjectsService.SUBJECTS.BUSINESS_STUDIES),
        OPERATIONS_MANAGEMENT("Operations Management", SubjectsService.SUBJECTS.BUSINESS_STUDIES),
        BUSINESS_FINANCE("Business Finance", SubjectsService.SUBJECTS.BUSINESS_STUDIES),
        BUSINESS_ETHICS("Business Ethics", SubjectsService.SUBJECTS.BUSINESS_STUDIES),
        ENTREPRENEURSHIP("Entrepreneurship", SubjectsService.SUBJECTS.BUSINESS_STUDIES),

        // Principles of Accounts Topics
        DOUBLE_ENTRY_BOOKKEEPING("Double Entry Bookkeeping", SubjectsService.SUBJECTS.PRINCIPLES_OF_ACCOUNTS),
        FINANCIAL_STATEMENTS("Financial Statements", SubjectsService.SUBJECTS.PRINCIPLES_OF_ACCOUNTS),
        PROFIT_LOSS_ACCOUNT("Profit and Loss Account", SubjectsService.SUBJECTS.PRINCIPLES_OF_ACCOUNTS),
        BALANCE_SHEET("Balance Sheet", SubjectsService.SUBJECTS.PRINCIPLES_OF_ACCOUNTS),
        CASH_FLOW_STATEMENT("Cash Flow Statement", SubjectsService.SUBJECTS.PRINCIPLES_OF_ACCOUNTS),
        RATIO_ANALYSIS("Ratio Analysis", SubjectsService.SUBJECTS.PRINCIPLES_OF_ACCOUNTS),
        DEPRECIATION("Depreciation", SubjectsService.SUBJECTS.PRINCIPLES_OF_ACCOUNTS),
        BAD_DEBTS_PROVISIONS("Bad Debts and Provisions", SubjectsService.SUBJECTS.PRINCIPLES_OF_ACCOUNTS),

        // General Paper Topics
        GLOBAL_ISSUES("Global Issues", SubjectsService.SUBJECTS.GENERAL_PAPER),
        SCIENCE_TECHNOLOGY("Science and Technology", SubjectsService.SUBJECTS.GENERAL_PAPER),
        MEDIA_SOCIETY("Media and Society", SubjectsService.SUBJECTS.GENERAL_PAPER),
        EDUCATION_YOUTH("Education and Youth", SubjectsService.SUBJECTS.GENERAL_PAPER),
        ENVIRONMENT_SUSTAINABILITY("Environment and Sustainability", SubjectsService.SUBJECTS.GENERAL_PAPER),
        POLITICS_GOVERNANCE("Politics and Governance", SubjectsService.SUBJECTS.GENERAL_PAPER),
        ARTS_CULTURE("Arts and Culture", SubjectsService.SUBJECTS.GENERAL_PAPER),
        ECONOMICS_SOCIETY("Economics and Society", SubjectsService.SUBJECTS.GENERAL_PAPER),

        // International School Topics (Additional)
        WORLD_LANGUAGES("World Languages", SubjectsService.SUBJECTS.ENGLISH),
        STEM_INTEGRATION("STEM Integration", SubjectsService.SUBJECTS.SCIENCE_INTL, SubjectsService.SUBJECTS.MATHEMATICS_INTL),
        CRITICAL_THINKING("Critical Thinking", SubjectsService.SUBJECTS.ENGLISH, SubjectsService.SUBJECTS.SOCIAL_STUDIES_INTL),
        RESEARCH_SKILLS("Research Skills", SubjectsService.SUBJECTS.ENGLISH, SubjectsService.SUBJECTS.SOCIAL_STUDIES_INTL),
        PRESENTATION_SKILLS("Presentation Skills", SubjectsService.SUBJECTS.ENGLISH),
        COLLABORATIVE_LEARNING("Collaborative Learning", SubjectsService.SUBJECTS.SOCIAL_STUDIES_INTL),
        ENVIRONMENTAL_SCIENCE("Environmental Science", SubjectsService.SUBJECTS.SCIENCE_INTL, SubjectsService.SUBJECTS.BIOLOGY_INTL),
        CULTURAL_STUDIES("Cultural Studies", SubjectsService.SUBJECTS.SOCIAL_STUDIES_INTL, SubjectsService.SUBJECTS.WORLD_HISTORY),
        DIGITAL_CITIZENSHIP("Digital Citizenship", SubjectsService.SUBJECTS.ENGLISH, SubjectsService.SUBJECTS.SOCIAL_STUDIES_INTL);

        private final String name;
        private final List<SubjectsService.SUBJECTS> applicableSubjects;

        TOPICS(String name, SubjectsService.SUBJECTS... subjects) {
            this.name = name;
            this.applicableSubjects = Arrays.asList(subjects);
        }

        public String getName() {
            return name;
        }

        public List<SubjectsService.SUBJECTS> getApplicableSubjects() {
            return applicableSubjects;
        }

        public boolean isApplicableToSubject(SubjectsService.SUBJECTS subject) {
            return applicableSubjects.contains(subject);
        }

        /**
         * Get all topics for a specific subject.
         */
        public static List<TOPICS> getTopicsBySubject(SubjectsService.SUBJECTS subject) {
            return Arrays.stream(values())
                    .filter(topic -> topic.isApplicableToSubject(subject))
                    .toList();
        }

        /**
         * Get all topics for a specific school level.
         */
        public static List<TOPICS> getTopicsByLevel(LevelsService.SchoolLevel level) {
            return Arrays.stream(values())
                    .filter(topic -> topic.getApplicableSubjects().stream()
                            .anyMatch(subject -> subject.isApplicableToLevel(level)))
                    .toList();
        }

        /**
         * Get all topics for a specific school category.
         */
        public static List<TOPICS> getTopicsByCategory(School.SchoolCategory category) {
            return Arrays.stream(values())
                    .filter(topic -> topic.getApplicableSubjects().stream()
                            .anyMatch(subject -> subject.getApplicableLevels().stream()
                                    .anyMatch(level -> level.getCategory() == category)))
                    .toList();
        }
    }

    private final TopicRepository topicRepository;
    private final TopicSubjectAttachmentRepository topicSubjectAttachmentRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public TopicsService(TopicRepository topicRepository, TopicSubjectAttachmentRepository topicSubjectAttachmentRepository,
                         SubjectRepository subjectRepository) {
        this.topicRepository = topicRepository;
        this.topicSubjectAttachmentRepository = topicSubjectAttachmentRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public JpaRepository<Topic, UUID> getRepository() {
        return this.topicRepository;
    }

    @Transactional
    public List<Topic> createDefaultTopics(UUID institutionId) {
        return Arrays.stream(TOPICS.values())
                .map(topicEnum -> {
                    List<String> subjectNames = topicEnum
                            .getApplicableSubjects()
                            .stream()
                            .map(SubjectsService.SUBJECTS::getName)
                            .toList();
                    List<Subject> subjects = subjectRepository.findByNameIn(subjectNames);
                    Topic topic = Topic
                            .builder()
                            .tenantId(institutionId.toString())
                            .name(topicEnum.getName())
                            .build();
                    create(topic);
                    List<TopicSubjectAttachment> topicSubjectAttachments = subjects
                            .stream()
                            .map(subject -> Attachment.createAndSync(topic, subject, new TopicSubjectAttachment()))
                            .toList();
                    topicSubjectAttachmentRepository.saveAllAndFlush(topicSubjectAttachments);
                    return topic;
                })
                .toList();
    }


    public Collection<Topic> getAllTopicsFromInstitution() {
        return topicRepository.findAll();
    }

    public TopicSubjectAttachment attach(UUID topicId, UUID subjectId) {
        Topic topic = findById(topicId);
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(() -> new ResourceNotFoundException("subject not found"));
        return topicSubjectAttachmentRepository.saveAndFlush(Attachment.createAndSync(topic, subject, new TopicSubjectAttachment()));
    }
}
