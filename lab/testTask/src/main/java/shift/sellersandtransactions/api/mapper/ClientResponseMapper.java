package shift.sellersandtransactions.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ClientResponseMapper {

    @Mapping(source = "firstName", target = "name")
    @Mapping(source = "birthDate", target = "birthday")
    @Mapping(source = "createdAt", target = "creationTime")
    @Mapping(source = "updatedAt", target = "updateTime")
    @Mapping(source = "phone", target = "phone", qualifiedByName = "mapPhone")
    ClientResponseDto map(UserEntity entity);

    @Named("mapPhone")
    default int mapPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(phone);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Phone number is not a valid int: " + phone, e);
        }
    }
}
