package com.ssafy.forestkeeper.application.service.plogging;

import com.ssafy.forestkeeper.application.dto.request.plogging.CoordinatesDTO;
import com.ssafy.forestkeeper.application.dto.request.plogging.ExpRegisterRequestDTO;
import com.ssafy.forestkeeper.application.dto.request.plogging.PloggingRegisterRequestDTO;
import com.ssafy.forestkeeper.application.dto.response.plogging.MountainPloggingInfoResponseDTO;
import com.ssafy.forestkeeper.application.dto.response.plogging.PloggingDetailResponseDTO;
import com.ssafy.forestkeeper.application.dto.response.plogging.PloggingTotalInfoResponseDTO;
import com.ssafy.forestkeeper.application.dto.response.plogging.TrashCanListWrapperResponseDTO;
import com.ssafy.forestkeeper.domain.dao.image.Image;
import com.ssafy.forestkeeper.domain.dao.mountain.Mountain;
import com.ssafy.forestkeeper.domain.dao.mountain.MountainVisit;
import com.ssafy.forestkeeper.domain.dao.plogging.Plogging;
import com.ssafy.forestkeeper.domain.dao.user.User;
import com.ssafy.forestkeeper.domain.repository.image.ImageRepository;
import com.ssafy.forestkeeper.domain.repository.mountain.MountainRepository;
import com.ssafy.forestkeeper.domain.repository.mountain.MountainVisitRepository;
import com.ssafy.forestkeeper.domain.repository.plogging.PloggingRepository;
import com.ssafy.forestkeeper.domain.repository.trashcan.TrashCanRepository;
import com.ssafy.forestkeeper.domain.repository.user.UserRepository;
import com.ssafy.forestkeeper.exception.MountainNotFoundException;
import com.ssafy.forestkeeper.exception.PloggingNotFoundException;
import com.ssafy.forestkeeper.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PloggingServiceImpl implements PloggingService {

    private final PloggingRepository ploggingRepository;

    private final UserRepository userRepository;

    private final MountainRepository mountainRepository;

    private final TrashCanRepository trashCanRepository;

    private final ImageRepository imageRepository;

    private final MountainVisitRepository mountainVisitRepository;

    @Value("${cloud.aws.s3.hosting}")
    public String hosting;

    @Override
    public Plogging register(PloggingRegisterRequestDTO ploggingRegisterRequestDTO) {

        Mountain mountain = mountainRepository.findByCode(ploggingRegisterRequestDTO.getMountainCode())
                .orElseThrow(() -> new MountainNotFoundException("??? ????????? ???????????? ????????????."));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        Duration duration = Duration.between(
                LocalDateTime.parse(ploggingRegisterRequestDTO.getStartedAt(), formatter),
                LocalDateTime.parse(ploggingRegisterRequestDTO.getEndedAt(), formatter)
        );

        MountainVisit mountainVisit = mountainVisitRepository.findByMountainCode(mountain.getCode())
                .orElse(MountainVisit.builder()
                        .mountain(mountain)
                        .visitorCount(0)
                        .build()
                );

        mountainVisit.increaseCount();

        mountainVisitRepository.save(mountainVisit);

        Plogging plogging = Plogging.builder()
                .distance(ploggingRegisterRequestDTO.getDistance())
                .startedAt(LocalDateTime.parse(ploggingRegisterRequestDTO.getStartedAt(), formatter))
                .endedAt(LocalDateTime.parse(ploggingRegisterRequestDTO.getEndedAt(), formatter))
                .exp(0L)
                .durationTime(getDuration(duration))
                .user(userRepository.findByEmailAndDelete(SecurityContextHolder.getContext().getAuthentication().getName(), false)
                        .orElseThrow(() -> new UserNotFoundException("?????? ????????? ???????????? ????????????.")))
                .mountain(mountain)
                .coords(getCoords(ploggingRegisterRequestDTO.getCoords()))
                .build();

        return ploggingRepository.save(plogging);

    }

    public String getCoords(List<CoordinatesDTO> list) {

        StringBuilder sb = new StringBuilder();

        for (CoordinatesDTO c : list) {
            sb.append(c.getX()).append(",").append(c.getY()).append("/");
        }

        return sb.toString();

    }

    public String getDuration(Duration duration) {

        StringBuilder sb = new StringBuilder();

        String HM = duration.toString().split("T")[1];

        int M;

        if (duration.toString().contains("H")) {
            sb.append(HM.split("H")[0]).append(" : ");

            M = Integer.parseInt(HM.split("H")[1].split("M")[0]);

            if (M < 10) sb.append(0).append(M);
            else sb.append(M);
        } else if (duration.toString().contains("M")) {
            sb.append("0 : ");

            M = Integer.parseInt(HM.split("M")[0]);

            if (M < 10) sb.append(0).append(M);
            else sb.append(M);
        } else {
            sb.append("0 : 00");
        }

        return sb.toString();

    }

    @Override
    public PloggingDetailResponseDTO get(String ploggingId) {

        Plogging plogging = ploggingRepository.findById(ploggingId)
                .orElseThrow(() -> new PloggingNotFoundException("????????? ????????? ???????????? ????????????."));

        Image image = imageRepository.findByPlogging(plogging).orElse(null);

        String imagePath;

        if (image == null) imagePath = "";
        else imagePath = hosting + "plogging/" + image.getSavedFileName();

        List<CoordinatesDTO> list = new ArrayList<>();

        String[] coords = plogging.getCoords().split("/");
        String[] xy;

        for (String s : coords) {
            xy = s.split(",");

            list.add(new CoordinatesDTO(xy[0], xy[1], xy[0], xy[1]));
        }

        return PloggingDetailResponseDTO.builder()
                .date(plogging.getStartedAt().toLocalDate().toString())
                .mountainName(plogging.getMountain().getName())
                .distance(plogging.getDistance())
                .time(plogging.getDurationTime())
                .exp(plogging.getExp())
                .imagePath(imagePath)
                .coords(list)
                .build();

    }

    @Override
    public void registerExp(ExpRegisterRequestDTO expRegisterRequestDTO) {

        Plogging plogging = ploggingRepository.findById(expRegisterRequestDTO.getPloggingId())
                .orElseThrow(() -> new PloggingNotFoundException("????????? ????????? ???????????? ????????????."));

        plogging.changeExp(expRegisterRequestDTO.getExp());

        ploggingRepository.save(plogging);

    }

    @Override
    public TrashCanListWrapperResponseDTO getTrashCanList() {

        return TrashCanListWrapperResponseDTO.builder()
                .trashCanList(trashCanRepository.findAll())
                .build();

    }

    @Override
    public TrashCanListWrapperResponseDTO getTrashCanList(String region) {

        return TrashCanListWrapperResponseDTO.builder()
                .trashCanList(trashCanRepository.findByRegion(region).orElse(null))
                .build();

    }

    @Override
    public void registerPloggingImg(String originalFileName, String savedFileName, String ploggingId) {

        Plogging plogging = ploggingRepository.findById(ploggingId)
                .orElseThrow(() -> new PloggingNotFoundException("????????? ????????? ???????????? ????????????."));

        imageRepository.save(Image.builder()
                .originalFileName(originalFileName)
                .savedFileName(savedFileName)
                .plogging(plogging)
                .build());

    }

    // ??? ?????? ????????? ????????? ?????? ??????
    @Override
    public MountainPloggingInfoResponseDTO getMountainPlogging(String mountainCode) {

        Mountain mountain = mountainRepository.findByCode(mountainCode)
                .orElseThrow(() -> new MountainNotFoundException("??? ????????? ???????????? ????????????."));

        User user = userRepository.findByEmailAndDelete(SecurityContextHolder.getContext().getAuthentication().getName(), false)
                .orElseThrow(() -> new UserNotFoundException("?????? ????????? ???????????? ????????????."));

        List<Plogging> ploggingList = ploggingRepository.findByMountain(mountain).orElse(null);

        long distance = 0L;

        int visitor = 0;

        List<Plogging> visitList = ploggingRepository.findByUserAndMountainOrderByStartedAtDesc(user, mountain).orElse(new ArrayList<>());

        if (ploggingList == null) {
            return MountainPloggingInfoResponseDTO.builder()
                    .distance(distance)
                    .visitor(visitor)
                    .count(visitList.size())
                    .build();
        }

        for (Plogging plogging : ploggingList) {
            visitor++;

            distance += (int) plogging.getDistance();
        }

        return MountainPloggingInfoResponseDTO.builder()
                .distance(distance)
                .visitor(visitor)
                .count(visitList.size())
                .build();

    }

    @Override
    public PloggingTotalInfoResponseDTO getTotalInfo() {

        List<Plogging> list = ploggingRepository.findAll();

        long num = list.size();

        long distance = 0;

        for (Plogging plogging : list) {
            distance += plogging.getDistance();
        }

        return PloggingTotalInfoResponseDTO.builder()
                .numberOfUsers(num)
                .totalDistance(distance)
                .build();

    }

}
