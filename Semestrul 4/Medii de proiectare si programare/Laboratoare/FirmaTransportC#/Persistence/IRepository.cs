using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using FirmaTransportC_.Model;
namespace FirmaTransportC_.Persistence
{
    public interface IRepository<ID, E> where E : Entity<ID>
    {
        ID Add(E entity);
        void Delete(ID id);
        void Update(E entity);
        E FindOne(ID id);
        List<E> FindAll();
    }
}
