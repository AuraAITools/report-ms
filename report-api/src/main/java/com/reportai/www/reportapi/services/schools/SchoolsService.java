package com.reportai.www.reportapi.services.schools;

import com.reportai.www.reportapi.entities.School;
import com.reportai.www.reportapi.entities.School.SchoolCategory;
import com.reportai.www.reportapi.repositories.SchoolRepository;
import com.reportai.www.reportapi.services.common.ISimpleRead;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SchoolsService implements ISimpleRead<School> {
    private final SchoolRepository schoolRepository;

    public SchoolsService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    @Override
    public JpaRepository<School, UUID> getRepository() {
        return this.schoolRepository;
    }

    public enum SINGAPORE_SCHOOLS {
        // Primary Schools - Government
        ADMIRALTY_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        AHMAD_IBRAHIM_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        ANCHOR_GREEN_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        ANDERSON_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        ANG_MO_KIO_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        ANGSANA_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        BALESTIER_HILL_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        BEACON_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        BEDOK_GREEN_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        BENDEMEER_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        BLANGAH_RISE_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        BOON_LAY_GARDEN_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        BUKIT_PANJANG_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        BUKIT_TIMAH_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        BUKIT_VIEW_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        CANBERRA_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        CANTONMENT_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        CASUARINA_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        CEDAR_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        CHONGFU_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),

        // Primary Schools - Government-Aided
        AL_IRSYAD_ISLAMIC_SCHOOL(SchoolCategory.PRIMARY),
        ANGLO_CHINESE_SCHOOL_JUNIOR(SchoolCategory.PRIMARY),
        ANGLO_CHINESE_SCHOOL_PRIMARY(SchoolCategory.PRIMARY),
        CANOSSA_CATHOLIC_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        CHIJ_OUR_LADY_OF_GOOD_COUNSEL(SchoolCategory.PRIMARY),
        CHIJ_OUR_LADY_OF_THE_NATIVITY(SchoolCategory.PRIMARY),
        CHIJ_PRIMARY_TOA_PAYOH(SchoolCategory.PRIMARY),
        CHIJ_ST_NICHOLAS_GIRLS_PRIMARY(SchoolCategory.PRIMARY),
        HOLY_INNOCENTS_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        KONG_HWA_SCHOOL(SchoolCategory.PRIMARY),
        MAHA_BODHI_SCHOOL(SchoolCategory.PRIMARY),
        METHODIST_GIRLS_PRIMARY(SchoolCategory.PRIMARY),
        NANYANG_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),
        PAYA_LEBAR_METHODIST_GIRLS_PRIMARY(SchoolCategory.PRIMARY),
        RAFFLES_GIRLS_PRIMARY_SCHOOL(SchoolCategory.PRIMARY),

        // Secondary Schools - Government
        ADMIRALTY_SECONDARY_SCHOOL(SchoolCategory.SECONDARY),
        AHMAD_IBRAHIM_SECONDARY_SCHOOL(SchoolCategory.SECONDARY),
        ANDERSON_SECONDARY_SCHOOL(SchoolCategory.SECONDARY),
        ANG_MO_KIO_SECONDARY_SCHOOL(SchoolCategory.SECONDARY),
        BARTLEY_SECONDARY_SCHOOL(SchoolCategory.SECONDARY),
        BEATTY_SECONDARY_SCHOOL(SchoolCategory.SECONDARY),
        BEDOK_GREEN_SECONDARY_SCHOOL(SchoolCategory.SECONDARY),
        BEDOK_SOUTH_SECONDARY_SCHOOL(SchoolCategory.SECONDARY),
        BEDOK_VIEW_SECONDARY_SCHOOL(SchoolCategory.SECONDARY),
        BENDEMEER_SECONDARY_SCHOOL(SchoolCategory.SECONDARY),
        BISHAN_PARK_SECONDARY_SCHOOL(SchoolCategory.SECONDARY),
        BOWEN_SECONDARY_SCHOOL(SchoolCategory.SECONDARY),
        BUKIT_BATOK_SECONDARY_SCHOOL(SchoolCategory.SECONDARY),
        BUKIT_MERAH_SECONDARY_SCHOOL(SchoolCategory.SECONDARY),
        BUKIT_PANJANG_GOVERNMENT_HIGH_SCHOOL(SchoolCategory.SECONDARY),

        // Secondary Schools - Government-Aided
        ANGLICAN_HIGH_SCHOOL(SchoolCategory.SECONDARY),
        ANGLO_CHINESE_SCHOOL_BARKER_ROAD(SchoolCategory.SECONDARY),
        CATHOLIC_HIGH_SCHOOL(SchoolCategory.SECONDARY),
        CHIJ_KATONG_CONVENT(SchoolCategory.SECONDARY),
        CHIJ_SECONDARY_TOA_PAYOH(SchoolCategory.SECONDARY),
        CHIJ_ST_JOSEPHS_CONVENT(SchoolCategory.SECONDARY),
        CHIJ_ST_NICHOLAS_GIRLS_SECONDARY(SchoolCategory.SECONDARY),
        CHIJ_ST_THERESAS_CONVENT(SchoolCategory.SECONDARY),
        CHRIST_CHURCH_SECONDARY_SCHOOL(SchoolCategory.SECONDARY),
        HOLY_INNOCENTS_HIGH_SCHOOL(SchoolCategory.SECONDARY),
        KUO_CHUAN_PRESBYTERIAN_SECONDARY(SchoolCategory.SECONDARY),
        MARIS_STELLA_HIGH_SCHOOL(SchoolCategory.SECONDARY),
        METHODIST_GIRLS_SECONDARY(SchoolCategory.SECONDARY),
        NAN_CHIAU_HIGH_SCHOOL(SchoolCategory.SECONDARY),
        NAN_HUA_HIGH_SCHOOL(SchoolCategory.SECONDARY),

        // Secondary Schools - Independent
        ANGLO_CHINESE_SCHOOL_INDEPENDENT(SchoolCategory.SECONDARY),
        HWA_CHONG_INSTITUTION_SECONDARY(SchoolCategory.SECONDARY),
        RAFFLES_GIRLS_SCHOOL_SECONDARY(SchoolCategory.SECONDARY),
        RAFFLES_INSTITUTION_SECONDARY(SchoolCategory.SECONDARY),
        SCHOOL_OF_SCIENCE_AND_TECHNOLOGY(SchoolCategory.SECONDARY),
        SINGAPORE_CHINESE_GIRLS_SCHOOL_SECONDARY(SchoolCategory.SECONDARY),
        SINGAPORE_SPORTS_SCHOOL(SchoolCategory.SECONDARY),
        NUS_HIGH_SCHOOL(SchoolCategory.SECONDARY),
        SCHOOL_OF_THE_ARTS(SchoolCategory.SECONDARY),

        // Junior Colleges - Government
        ANDERSON_SERANGOON_JUNIOR_COLLEGE(SchoolCategory.JUNIOR_COLLEGE),
        ANGLO_CHINESE_JUNIOR_COLLEGE(SchoolCategory.JUNIOR_COLLEGE),
        CATHOLIC_JUNIOR_COLLEGE(SchoolCategory.JUNIOR_COLLEGE),
        EUNOIA_JUNIOR_COLLEGE(SchoolCategory.JUNIOR_COLLEGE),
        JURONG_PIONEER_JUNIOR_COLLEGE(SchoolCategory.JUNIOR_COLLEGE),
        NATIONAL_JUNIOR_COLLEGE(SchoolCategory.JUNIOR_COLLEGE),
        TAMPINES_MERIDIAN_JUNIOR_COLLEGE(SchoolCategory.JUNIOR_COLLEGE),
        TEMASEK_JUNIOR_COLLEGE(SchoolCategory.JUNIOR_COLLEGE),
        VICTORIA_JUNIOR_COLLEGE(SchoolCategory.JUNIOR_COLLEGE),
        YISHUN_INNOVA_JUNIOR_COLLEGE(SchoolCategory.JUNIOR_COLLEGE),

        // Junior Colleges - Independent
        HWA_CHONG_INSTITUTION_JC(SchoolCategory.JUNIOR_COLLEGE),
        RAFFLES_INSTITUTION_JC(SchoolCategory.JUNIOR_COLLEGE),
        ANGLO_CHINESE_SCHOOL_INDEPENDENT_JC(SchoolCategory.JUNIOR_COLLEGE),
        ST_JOSEPHS_INSTITUTION_JC(SchoolCategory.JUNIOR_COLLEGE),

        // Polytechnics
        SINGAPORE_POLYTECHNIC(SchoolCategory.POLYTECHNIC),
        NGEE_ANN_POLYTECHNIC(SchoolCategory.POLYTECHNIC),
        TEMASEK_POLYTECHNIC(SchoolCategory.POLYTECHNIC),
        NANYANG_POLYTECHNIC(SchoolCategory.POLYTECHNIC),
        REPUBLIC_POLYTECHNIC(SchoolCategory.POLYTECHNIC),

        // International Schools
        ANGLO_CHINESE_SCHOOL_INTERNATIONAL(SchoolCategory.INTERNATIONAL),
        AUSTRALIAN_INTERNATIONAL_SCHOOL(SchoolCategory.INTERNATIONAL),
        BRIGHTON_COLLEGE_SINGAPORE(SchoolCategory.INTERNATIONAL),
        CANADIAN_INTERNATIONAL_SCHOOL(SchoolCategory.INTERNATIONAL),
        CHATSWORTH_INTERNATIONAL_SCHOOL(SchoolCategory.INTERNATIONAL),
        DULWICH_COLLEGE_SINGAPORE(SchoolCategory.INTERNATIONAL),
        DOVER_COURT_INTERNATIONAL_SCHOOL(SchoolCategory.INTERNATIONAL),
        ETONHOUSE_INTERNATIONAL_SCHOOL(SchoolCategory.INTERNATIONAL),
        GERMAN_EUROPEAN_SCHOOL_SINGAPORE(SchoolCategory.INTERNATIONAL),
        GESS_INTERNATIONAL_SCHOOL(SchoolCategory.INTERNATIONAL),
        GLOBAL_INDIAN_INTERNATIONAL_SCHOOL(SchoolCategory.INTERNATIONAL),
        HOLLAND_INTERNATIONAL_SCHOOL(SchoolCategory.INTERNATIONAL),
        INSWORLD_INSTITUTE(SchoolCategory.INTERNATIONAL),
        INTERNATIONAL_COMMUNITY_SCHOOL(SchoolCategory.INTERNATIONAL),
        INTERNATIONAL_FRENCH_SCHOOL_SINGAPORE(SchoolCategory.INTERNATIONAL),

        // Technical Education
        ITE_COLLEGE_CENTRAL(SchoolCategory.TECHNICAL),
        ITE_COLLEGE_EAST(SchoolCategory.TECHNICAL),
        ITE_COLLEGE_WEST(SchoolCategory.TECHNICAL),

        // Arts Institutions
        LASALLE_COLLEGE_OF_THE_ARTS(SchoolCategory.ARTS),
        NANYANG_ACADEMY_OF_FINE_ARTS(SchoolCategory.ARTS),
        UNIVERSITY_OF_THE_ARTS_SINGAPORE(SchoolCategory.ARTS),

        // Universities
        NATIONAL_UNIVERSITY_OF_SINGAPORE(SchoolCategory.UNIVERSITY),
        NANYANG_TECHNOLOGICAL_UNIVERSITY(SchoolCategory.UNIVERSITY),
        SINGAPORE_MANAGEMENT_UNIVERSITY(SchoolCategory.UNIVERSITY),
        SINGAPORE_UNIVERSITY_OF_TECHNOLOGY_AND_DESIGN(SchoolCategory.UNIVERSITY),
        SINGAPORE_INSTITUTE_OF_TECHNOLOGY(SchoolCategory.UNIVERSITY),
        SINGAPORE_UNIVERSITY_OF_SOCIAL_SCIENCES(SchoolCategory.UNIVERSITY);

        private final SchoolCategory category;

        SINGAPORE_SCHOOLS(SchoolCategory category) {
            this.category = category;
        }
    }

    public static final List<SINGAPORE_SCHOOLS> DEFAULT_SINGAPORE_SCHOOLS = Arrays.asList(SINGAPORE_SCHOOLS.values());


    /**
     * Creates default schools for the given institution
     *
     * @param institutionId
     * @return
     */
    public List<School> createDefaultSchools(UUID institutionId) {
        Objects.requireNonNull(institutionId, "institutionId cannot be null");
        List<School> singaporeSchools = DEFAULT_SINGAPORE_SCHOOLS
                .stream()
                .map(school -> {
                    School newSchool = new School();
                    newSchool.setName(school.name());
                    newSchool.setSchoolCategory(school.category);
                    newSchool.setTenantId(institutionId.toString());
                    return newSchool;
                })
                .toList();
        return schoolRepository.saveAllAndFlush(singaporeSchools);
    }
}
