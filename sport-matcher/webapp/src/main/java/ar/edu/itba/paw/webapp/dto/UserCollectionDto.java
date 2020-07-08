package ar.edu.itba.paw.webapp.dto;

import java.util.List;

public class UserCollectionDto {
	private List<UserDto> users;

	public static UserCollectionDto ofUsers(List<UserDto> users) {
		UserCollectionDto dto = new UserCollectionDto();
		dto.users = users;

		return dto;
	}

	public List<UserDto> getUsers() {
		return users;
	}

	public void setUsers(List<UserDto> users) {
		this.users = users;
	}
	
}
