package shift.sellersandtransactions.api.contoller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shift.sellersandtransactions.api.dto.ClientResponseDto;
import shift.sellersandtransactions.core.service.ClientService;

import java.util.List;

@RestController
@RequestMapping(Paths.CLIENTS_PREFIX)
public class ClientsController {
    private final ClientService clientService;

    public ClientsController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<ClientResponseDto>> getClients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Integer phone,
            @RequestParam(defaultValue = "100") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset
    ) {
        List<ClientResponseDto> clients = clientService.getClients(name, lastName, email, phone, limit, offset);
        return ResponseEntity.ok(clients);
    }

}
