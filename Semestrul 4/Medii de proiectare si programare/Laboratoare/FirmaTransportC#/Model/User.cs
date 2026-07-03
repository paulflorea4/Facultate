namespace FirmaTransportC_.Model
{
    public class User : Entity<long>
    {
        private string username;
        private string password;
        public User(long id, string username, string password) : base(id)
        {
            this.username = username;
            this.password = password;
        }
        public string Username
        {
            get { return username; }
            set { username = value; }
        }
        public string Password
        {
            get { return password; }
            set { password = value; }
        }

        public override string ToString()
        {
            return "User{" +
                "id=" + Id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
        }
    }
}
