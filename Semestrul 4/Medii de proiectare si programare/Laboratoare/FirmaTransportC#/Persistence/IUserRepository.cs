using FirmaTransportC_.Model;
using System;
using System.Collections.Generic;
using System.Text;

namespace FirmaTransportC_.Persistence
{
    public interface IUserRepository : IRepository<long, User>
    {
        User? FindByUsername(string username);
    }
}
