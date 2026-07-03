using System.Text.Json.Serialization;

namespace FirmaTransportC_.Networking.dto
{
    [Serializable]
    public class UserDTO
    {
        public long Id { get; set; }
        public string Username { get; set; }
        public string Password { get; set; }

        public UserDTO()
        { 
        }

        public UserDTO(long id, string username, string password)
        {
            Id = id;
            Username = username;
            Password = password;
        }
    }
}
