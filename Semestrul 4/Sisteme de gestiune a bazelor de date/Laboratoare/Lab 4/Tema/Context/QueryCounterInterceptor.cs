using Microsoft.EntityFrameworkCore.Diagnostics;
using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Text;

namespace Tema.Context
{
    public class QueryCounterInterceptor : DbCommandInterceptor
    {
        public int QueryCount { get; set; } = 0;
        public void Reset() => QueryCount = 0;
        public override InterceptionResult<DbDataReader> ReaderExecuting(DbCommand command, CommandEventData eventData, InterceptionResult<DbDataReader> result)
        {
            QueryCount++;
            return base.ReaderExecuting(command, eventData, result);
        }
    }
}
