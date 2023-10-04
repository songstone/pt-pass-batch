package com.song.pass.domain.packaze;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class PackageRepositoryTest {

    private final PackageRepository packageRepository;

    public PackageRepositoryTest(@Autowired PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Test
    void save() {
        //given
        Package packageEntity = Package.builder()
            .packageName("바디 챌린지 PT 12주")
            .period(84)
            .build();

        //when
        packageRepository.save(packageEntity);

        //then
        assertThat(packageEntity.getPackageSeq()).isNotNull();
    }

    @Test
    void findByCreatedAtAfter() {
        //given
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);

        Package packageEntity1 = Package.builder()
            .packageName("학생 전용 3개월")
            .period(90)
            .build();

        Package packageEntity2 = Package.builder()
            .packageName("학생 전용 6개월")
            .period(180)
            .build();

        packageRepository.save(packageEntity1);
        packageRepository.save(packageEntity2);

        //when
        List<Package> packages = packageRepository.findByCreatedAtAfter(dateTime, PageRequest.of(0, 1, Sort.by("packageSeq").descending()));

        //then
        assertThat(packages.size()).isEqualTo(1);
        assertThat(packageEntity2.getPackageSeq()).isEqualTo(packages.get(0).getPackageSeq());
    }

    @Test
    void updateCountAndPeriod() {
        //given
        Package packageEntity = Package.builder()
            .packageName("바디 프로필 이벤트 4개월")
            .period(90)
            .build();
        packageRepository.save(packageEntity);

        //when
        int updateCount = packageRepository.updateCountAndPeriod(packageEntity.getPackageSeq(), 30, 120);
        Package findPackage = packageRepository.findById(packageEntity.getPackageSeq()).get();

        //then
        assertThat(updateCount).isEqualTo(1);
        assertThat(findPackage.getCount()).isEqualTo(30);
        assertThat(findPackage.getPeriod()).isEqualTo(120);
    }

    @Test
    void delete() {
        //given
        Package packageEntity = Package.builder()
            .packageName("제거 이용권")
            .count(1)
            .build();
        Package newPackageEntity = packageRepository.save(packageEntity);

        //when
        packageRepository.deleteById(newPackageEntity.getPackageSeq());

        //then
        assertThat(packageRepository.findById(newPackageEntity.getPackageSeq())).isEmpty();
    }
}