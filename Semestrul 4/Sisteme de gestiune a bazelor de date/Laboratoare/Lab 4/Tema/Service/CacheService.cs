using Microsoft.Extensions.Caching.Memory;
using System;
using System.Collections.Generic;
using System.Text;
using Tema.Context;
using Tema.Domain;

namespace Tema.Service
{
    public class CacheService
    {
        private readonly IMemoryCache _cache = new MemoryCache(new MemoryCacheOptions());
        public int CacheHits { get; private set; } = 0;
        public int CacheMisses { get; private set; } = 0;

        public Department GetDepartmentById(int id)
        {
            if (_cache.TryGetValue($"dept_{id}", out Department dept))
            {
                CacheHits++;
                return dept;
            }

            CacheMisses++;
            using var context = new MyContext();
            dept = context.Departments.Find(id);

            if (dept != null)
            {
                _cache.Set($"dept_{id}", dept, TimeSpan.FromMinutes(5));
            }
            return dept;
        }

        public void UpdateDepartment(Department department)
        {
            using var context = new MyContext();
            context.Departments.Update(department);
            context.SaveChanges();

            _cache.Remove($"dept_{department.Id}");
        }
    }
}
