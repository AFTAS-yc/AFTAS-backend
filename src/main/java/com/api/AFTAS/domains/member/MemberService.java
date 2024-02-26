package com.api.AFTAS.domains.member;


import com.api.AFTAS.domains.member.DTOs.MemberReqDTO;
import com.api.AFTAS.domains.member.DTOs.MemberRespDTO;
import com.api.AFTAS.security.User.User;
import com.api.AFTAS.security.User.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MemberService implements MemberServiceInterface {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public MemberRespDTO create(MemberReqDTO memberReqDTO) {
        User member = modelMapper.map(memberReqDTO, User.class);
        member.setAccessionDate(LocalDate.now());
            member = userRepository.save(member);
            return modelMapper.map(member, MemberRespDTO.class);
    }

    @Override
    public MemberRespDTO update(MemberReqDTO memberReqDTO, Integer num) {
        Optional<User> existUser = userRepository.findById(num);
        if (existUser.isPresent()) {
            memberReqDTO.setId(existUser.get().getId());
            return modelMapper.map(userRepository.save(modelMapper.map(memberReqDTO, User.class)), MemberRespDTO.class);
        }
        return null;
    }

    @Override
    public Integer delete(Integer num) {
        Optional<User> member = userRepository.findById(num);
        if(member.isPresent()) {
            userRepository.delete(member.get());
            return 1;
        }else return 0;
    }

    @Override
    public List<MemberRespDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(member -> modelMapper.map(member,MemberRespDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public MemberRespDTO getOne(Integer num) {
        Optional<User> member = userRepository.findById(num);
        return member.map(value -> modelMapper.map(value, MemberRespDTO.class)).orElse(null);
    }
}
