using Bogus;
using Microsoft.EntityFrameworkCore;
using System.Text;
using Tema.Context;
using Tema.Domain;
using Tema.Service;

namespace Tema
{
    public partial class Form1 : Form
    {
        private double emailTime = 0, emailTimeIdx = 0;
        private double deptTime = 0, deptTimeIdx = 0;
        private double salaryTime = 0, salaryTimeIdx = 0;
        private double multiTime = 0, multiTimeIdx = 0;
        private bool isIndexPresent = false;

        private int currentPage = 1;
        private int pageSize = 10;
        private bool isOffset = false;

        private CacheService _cacheService = new CacheService();

        public Form1()
        {
            InitializeComponent();
            InitializeIndexGrid();
        }

        private void InitializeIndexGrid()
        {
            indexResultsDgv.Columns.Clear();
            indexResultsDgv.Columns.Add("QueryType", "Interogare");
            indexResultsDgv.Columns.Add("WithoutIndex", "Fără Index (ms)");
            indexResultsDgv.Columns.Add("WithIndex", "Cu Index (ms)");
            indexResultsDgv.Columns.Add("Improvement", "Îmbunătățire");
            indexResultsDgv.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            indexResultsDgv.AllowUserToAddRows = false;
        }

        private string CalculateImprovement(double withoutIndex, double withIndex)
        {
            if (withIndex < 0.001) return "Instant";
            if (withIndex < 0.001 && withoutIndex < 0.001) return "1x";
            double improvement = withoutIndex / withIndex;
            return $"{Math.Round(improvement, 2)}x";
        }

        private void LoadIndexResults()
        {
            indexResultsDgv.Rows.Clear();
            indexResultsDgv.Rows.Add("Cautare email", emailTime.ToString("0.00"), emailTimeIdx.ToString("0.00"), CalculateImprovement(emailTime, emailTimeIdx));
            indexResultsDgv.Rows.Add("Cautare departament", deptTime.ToString("0.00"), deptTimeIdx.ToString("0.00"), CalculateImprovement(deptTime, deptTimeIdx));
            indexResultsDgv.Rows.Add("Interval salariu", salaryTime.ToString("0.00"), salaryTimeIdx.ToString("0.00"), CalculateImprovement(salaryTime, salaryTimeIdx));
            indexResultsDgv.Rows.Add("Multi-coloana", multiTime.ToString("0.00"), multiTimeIdx.ToString("0.00"), CalculateImprovement(multiTime, multiTimeIdx));
        }

        private void SeedDatabase()
        {
            using var context = new MyContext();

            context.Database.EnsureCreated();

            if (context.Departments.Any())
            {
                return;
            }

            var departmentFaker = new Faker<Department>()
                .RuleFor(d => d.Name, f => f.Commerce.Department());

            var departments = departmentFaker.Generate(50);
            context.Departments.AddRange(departments);
            context.SaveChanges();

            var deptIds = context.Departments.Select(d => d.Id).ToList();

            var employeeFaker = new Faker<Employee>()
                .RuleFor(e => e.Email, f => f.Internet.Email())
                .RuleFor(e => e.Salary, f => f.Random.Decimal(30000, 120000))
                .RuleFor(e => e.DepartmentId, f => f.PickRandom(deptIds));

            var employees = employeeFaker.Generate(10000);

            context.Employees.AddRange(employees);
            context.SaveChanges();
        }

        private void UpdateTotalEmployeesLabel()
        {
            using var context = new MyContext();
            int totalEmployees = context.Employees.Count();
            totalEmpLbl.Text = $"Total angajați: {totalEmployees}";
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            Cursor.Current = Cursors.WaitCursor;
            SeedDatabase();
            UpdateTotalEmployeesLabel();
            Cursor.Current = Cursors.Default;
        }

        private void n1TestBtn_Click(object sender, EventArgs e)
        {
            using var context = new MyContext();
            MyContext.Counter.Reset();
            var stopwatch = System.Diagnostics.Stopwatch.StartNew();

            var departmentsBad = context.Departments.ToList();
            foreach (var dept in departmentsBad)
            {
                var employees = dept.Employees.ToList();
            }
            stopwatch.Stop();
            n1LogsRtb.Text = $"Timp N+1: {stopwatch.ElapsedMilliseconds} ms\nNumar interogari: {MyContext.Counter.QueryCount}";

            MyContext.Counter.Reset();
            stopwatch.Restart();
            var departmentsGood = context.Departments
                                         .Include(d => d.Employees)
                                         .ToList();
            stopwatch.Stop();
            n1LogsRtb.Text += $"\nTimp Eager Loading: {stopwatch.ElapsedMilliseconds} ms\nNumar interogari: {MyContext.Counter.QueryCount}";
        }

        private string GetExplainAnalyze(string sqlQuery)
        {
            using var context = new MyContext();
            using var command = context.Database.GetDbConnection().CreateCommand();

            command.CommandText = $"EXPLAIN ANALYZE {sqlQuery}";

            context.Database.OpenConnection();
            using var reader = command.ExecuteReader();

            var sb = new StringBuilder();
            while (reader.Read())
            {
                sb.AppendLine(reader.GetString(0));
            }

            return sb.ToString();
        }

        private void addIndexBtn_Click(object sender, EventArgs e)
        {
            using var context = new MyContext();
            context.Database.ExecuteSqlRaw("CREATE INDEX IF NOT EXISTS idx_employees_email ON \"employees\" (\"email\");");
            context.Database.ExecuteSqlRaw("CREATE INDEX IF NOT EXISTS idx_employees_department_id ON \"employees\" (\"department_id\");");
            context.Database.ExecuteSqlRaw("CREATE INDEX IF NOT EXISTS idx_employees_salary ON \"employees\" (\"salary\");");
            context.Database.ExecuteSqlRaw("CREATE INDEX IF NOT EXISTS idx_employees_dept_salary ON \"employees\" (\"department_id\", \"salary\");");
            isIndexPresent = true;
        }

        private void rmvIndexBtn_Click(object sender, EventArgs e)
        {
            using var context = new MyContext();
            context.Database.ExecuteSqlRaw("DROP INDEX IF EXISTS idx_employees_email;");
            context.Database.ExecuteSqlRaw("DROP INDEX IF EXISTS idx_employees_department_id;");
            context.Database.ExecuteSqlRaw("DROP INDEX IF EXISTS idx_employees_salary;");
            context.Database.ExecuteSqlRaw("DROP INDEX IF EXISTS idx_employees_dept_salary;");
            isIndexPresent = false;
        }

        private void emailBtn_Click(object sender, EventArgs e)
        {
            using var context = new MyContext();

            var stopwatch = System.Diagnostics.Stopwatch.StartNew();
            for (int i = 0; i < 100; i++)
            {
                var result = context.Employees.Where(emp => emp.Email == "l.clark@engineering.com").ToList();
            }
            stopwatch.Stop();

            string plan = GetExplainAnalyze("SELECT * FROM \"employees\" WHERE \"email\" = 'l.clark@engineering.com'");
            expAnlRtb.Text = plan;

            if (isIndexPresent)
            {
                emailTimeIdx = stopwatch.Elapsed.TotalMilliseconds / 100;
            }
            else
            {
                emailTime = stopwatch.Elapsed.TotalMilliseconds / 100;
            }
            LoadIndexResults();
        }

        private void dptBtn_Click(object sender, EventArgs e)
        {
            using var context = new MyContext();

            var stopwatch = System.Diagnostics.Stopwatch.StartNew();
            for (int i = 0; i < 100; i++)
            {
                var result = context.Employees.Where(emp => emp.DepartmentId == 105).ToList();
            }
            stopwatch.Stop();

            string plan = GetExplainAnalyze("SELECT * FROM \"employees\" WHERE \"department_id\" = 105");
            expAnlRtb.Text = plan;

            if (isIndexPresent)
            {
                deptTimeIdx = stopwatch.Elapsed.TotalMilliseconds / 100;
            }
            else
            {
                deptTime = stopwatch.Elapsed.TotalMilliseconds / 100;
            }
            LoadIndexResults();
        }

        private void salaryBtn_Click(object sender, EventArgs e)
        {
            using var context = new MyContext();

            var stopwatch = System.Diagnostics.Stopwatch.StartNew();
            for (int i = 0; i < 100; i++)
            {
                var result = context.Employees.Where(emp => emp.Salary >= 50000 && emp.Salary <= 100000).ToList();
            }
            stopwatch.Stop();

            string plan = GetExplainAnalyze("SELECT * FROM \"employees\" WHERE \"salary\" >= 50000 AND \"salary\" <= 100000");
            expAnlRtb.Text = plan;

            if (isIndexPresent)
            {
                salaryTimeIdx = stopwatch.Elapsed.TotalMilliseconds / 100;
            }
            else
            {
                salaryTime = stopwatch.Elapsed.TotalMilliseconds / 100;
            }
            LoadIndexResults();
        }

        private void multiBtn_Click(object sender, EventArgs e)
        {
            using var context = new MyContext();

            var stopwatch = System.Diagnostics.Stopwatch.StartNew();
            for (int i = 0; i < 100; i++)
            {
                var result = context.Employees.Where(emp => emp.DepartmentId == 105 && emp.Salary > 80000).ToList();
            }
            stopwatch.Stop();

            string plan = GetExplainAnalyze("SELECT * FROM \"employees\" WHERE \"department_id\" = 105 AND \"salary\" > 80000");
            expAnlRtb.Text = plan;

            if (isIndexPresent)
            {
                multiTimeIdx = stopwatch.Elapsed.TotalMilliseconds / 100;
            }
            else
            {
                multiTime = stopwatch.Elapsed.TotalMilliseconds / 100;
            }
            LoadIndexResults();
        }

        public List<Employee> GetEmployeesPageOffset(int pageNumber, int pageSize)
        {
            using var context = new MyContext();
            int offset = (pageNumber - 1) * pageSize;

            explAnlRtb.Text = GetExplainAnalyze($"SELECT * FROM \"employees\" ORDER BY \"id\" OFFSET {offset} LIMIT {pageSize}");
            return context.Employees
                          .OrderBy(e => e.Id)
                          .Skip(offset)
                          .Take(pageSize)
                          .ToList();
        }

        public List<Employee> GetEmployeesPageKeyset(int lastId, int pageSize)
        {
            using var context = new MyContext();
            explAnlRtb.Text = GetExplainAnalyze($"SELECT * FROM \"employees\" WHERE \"id\" > {lastId} ORDER BY \"id\" LIMIT {pageSize}");
            return context.Employees
                          .Where(e => e.Id > lastId)
                          .OrderBy(e => e.Id)
                          .Take(pageSize)
                          .ToList();
        }

        private void pageSizeCmb_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (int.TryParse(pageSizeCmb.SelectedItem?.ToString(), out int newSize))
            {
                pageSize = newSize;
                currentPage = 1;
                LoadPageData();
            }
        }
        private void offsetCheck_CheckedChanged(object sender, EventArgs e)
        {
            isOffset = offsetCheck.Checked;
            LoadPageData();
        }

        private void prevBtn_Click(object sender, EventArgs e)
        {
            if (currentPage > 1)
            {
                currentPage--;
                LoadPageData();
            }
        }

        private void nextBtn_Click(object sender, EventArgs e)
        {
            currentPage++;
            LoadPageData();
        }

        private void LoadPageData()
        {
            List<Employee> employees;
            if (isOffset)
                employees = GetEmployeesPageOffset(currentPage, pageSize);
            else
                employees = GetEmployeesPageKeyset((currentPage - 1) * pageSize , pageSize);
            employeeDgv.DataSource = employees;
            pageInfoLbl.Text = $"Pagina {currentPage}";

        }

        private void cachingTestBtn_Click(object sender, EventArgs e)
        {
            int targetDeptId = 101;

            var stopwatch = System.Diagnostics.Stopwatch.StartNew();

            var dept = _cacheService.GetDepartmentById(targetDeptId);

            stopwatch.Stop();

            MessageBox.Show($"Departament găsit: {dept?.Name}\nTimp de răspuns: {stopwatch.ElapsedMilliseconds} ms");

            cachingHitsLbl.Text = $"Hits: {_cacheService.CacheHits}";
            cachingMissesLbl.Text = $"Misses: {_cacheService.CacheMisses}";
        }

        private void updateCacheBtn_Click(object sender, EventArgs e)
        {
            int targetDeptId = 101;
            var dept = _cacheService.GetDepartmentById(targetDeptId);

            if (dept != null)
            {
                dept.Name = "Departament Actualizat " + DateTime.Now.Second;
                _cacheService.UpdateDepartment(dept);

                cachingHitsLbl.Text = $"Hits: {_cacheService.CacheHits}";
                cachingMissesLbl.Text = $"Misses: {_cacheService.CacheMisses}";

                MessageBox.Show("Departament actualizat și cache invalidat!");
            }
        }

        public string RunBulkBenchmark(int deptId)
        {
            using var context = new MyContext();
            var sb = new System.Text.StringBuilder();
            var stopwatch = new System.Diagnostics.Stopwatch();

            // Resetăm datele la starea inițială pentru un test corect (opțional)

            // Abordarea 1: Individual
            stopwatch.Start();
            var employees1 = context.Employees.Where(e => e.DepartmentId == deptId).ToList();
            foreach (var emp in employees1) { emp.Salary *= 1.1m; }
            context.SaveChanges();
            stopwatch.Stop();
            sb.AppendLine($"Abordarea 1 (Individual): {stopwatch.ElapsedMilliseconds} ms");

            // Abordarea 2: ExecuteUpdate
            stopwatch.Restart();
            context.Employees
                   .Where(e => e.DepartmentId == deptId)
                   .ExecuteUpdate(s => s.SetProperty(e => e.Salary, e => e.Salary * 1.1m));
            stopwatch.Stop();
            sb.AppendLine($"Abordarea 2 (ExecuteUpdate): {stopwatch.ElapsedMilliseconds} ms");

            // Abordarea 3: Batch
            stopwatch.Restart();
            var employees3 = context.Employees.Where(e => e.DepartmentId == deptId).ToList();
            int batchSize = 50;
            for (int i = 0; i < employees3.Count; i++)
            {
                employees3[i].Salary *= 1.1m;
                if (i > 0 && i % batchSize == 0)
                {
                    context.SaveChanges();
                    context.ChangeTracker.Clear();
                }
            }
            context.SaveChanges();
            stopwatch.Stop();
            sb.AppendLine($"Abordarea 3 (Batch/Flush): {stopwatch.ElapsedMilliseconds} ms");

            return sb.ToString();
        }

        private void bulkRunBtn_Click(object sender, EventArgs e)
        {
            Cursor.Current = Cursors.WaitCursor;

            int deptIdToTest = 101;

            timesRtb.Text = RunBulkBenchmark(deptIdToTest);

            Cursor.Current = Cursors.Default;
        }

        private void button1_Click(object sender, EventArgs e)
        {
            using var context = new MyContext();
            var stopwatch = System.Diagnostics.Stopwatch.StartNew();

            for (long i = 1; i <= 1000; i++)
            {
                var emp = context.Employees.FirstOrDefault(e => e.Id == i);
            }
            stopwatch.Stop();
            long timeA = stopwatch.ElapsedMilliseconds;

            stopwatch.Restart();

            var compiledQuery = EF.CompileQuery((MyContext ctx, long id) =>
                ctx.Employees.FirstOrDefault(e => e.Id == id));

            for (long i = 1; i <= 1000; i++)
            {
                var emp = compiledQuery(context, i);
            }
            stopwatch.Stop();
            long timeB = stopwatch.ElapsedMilliseconds;

            MessageBox.Show($"Test A (Fără reutilizare): {timeA} ms\nTest B (Compiled Query): {timeB} ms");
        }
    }
}
