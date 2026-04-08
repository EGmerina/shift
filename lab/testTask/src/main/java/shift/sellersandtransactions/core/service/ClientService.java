package shift.sellersandtransactions.core.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shift.sellersandtransactions.api.mapper.ClientResponseMapper;
import shift.sellersandtransactions.core.repository.UserRepository;

import java.awt.print.Pageable;
import java.util.List;

@Service
public class ClientService {

    private final UserRepository userRepository;
    private final ClientResponseMapper mapper;

    public ClientService(UserRepository userRepository, ClientResponseMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<ClientResponseDto> getClients(String name,
                                              String lastName,
                                              String email,
                                              Integer phone,
                                              Integer limit,
                                              Integer offset) {

        String phoneStr = phone != null ? String.valueOf(phone) : null;

        List<UserEntity> users = userRepository.findByFilters(
                name,
                lastName,
                email,
                phoneStr,
                (Pageable) PageRequest.of(offset / limit, limit)
        );

        return users.stream()
                .map(mapper::map)
                .toList();
    }
}
