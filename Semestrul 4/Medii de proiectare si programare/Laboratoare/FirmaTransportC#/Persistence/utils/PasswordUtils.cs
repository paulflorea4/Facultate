namespace FirmaTransportC_.Persistence.utils
{
    public static class PasswordUtils
    {
        public static string Hash(string plain)
        {
            return BCrypt.Net.BCrypt.HashPassword(plain);
        }

        public static bool Verify(string plain, string hashed)
        {
            return BCrypt.Net.BCrypt.Verify(plain, hashed);
        }
    }
}
