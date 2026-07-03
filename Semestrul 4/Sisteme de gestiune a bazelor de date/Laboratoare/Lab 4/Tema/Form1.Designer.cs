namespace Tema
{
    partial class Form1
    {
        /// <summary>
        ///  Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        ///  Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        ///  Required method for Designer support - do not modify
        ///  the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            tabControl1 = new TabControl();
            tabPage1 = new TabPage();
            n1TestBtn = new Button();
            n1LogsRtb = new RichTextBox();
            tabPage2 = new TabPage();
            expAnlRtb = new RichTextBox();
            rmvIndexBtn = new Button();
            addIndexBtn = new Button();
            multiBtn = new Button();
            salaryBtn = new Button();
            dptBtn = new Button();
            emailBtn = new Button();
            indexResultsDgv = new DataGridView();
            tabPage3 = new TabPage();
            explAnlRtb = new RichTextBox();
            totalEmpLbl = new Label();
            offsetCheck = new CheckBox();
            pageSizeCmb = new ComboBox();
            pageInfoLbl = new Label();
            nextBtn = new Button();
            prevBtn = new Button();
            employeeDgv = new DataGridView();
            tabPage4 = new TabPage();
            button1 = new Button();
            cachingTestBtn = new Button();
            cachingMissesLbl = new Label();
            cachingHitsLbl = new Label();
            tabPage5 = new TabPage();
            bulkRunBtn = new Button();
            timesRtb = new RichTextBox();
            updateCacheBtn = new Button();
            tabControl1.SuspendLayout();
            tabPage1.SuspendLayout();
            tabPage2.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)indexResultsDgv).BeginInit();
            tabPage3.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)employeeDgv).BeginInit();
            tabPage4.SuspendLayout();
            tabPage5.SuspendLayout();
            SuspendLayout();
            // 
            // tabControl1
            // 
            tabControl1.Controls.Add(tabPage1);
            tabControl1.Controls.Add(tabPage2);
            tabControl1.Controls.Add(tabPage3);
            tabControl1.Controls.Add(tabPage4);
            tabControl1.Controls.Add(tabPage5);
            tabControl1.Dock = DockStyle.Fill;
            tabControl1.Location = new Point(0, 0);
            tabControl1.Name = "tabControl1";
            tabControl1.SelectedIndex = 0;
            tabControl1.Size = new Size(800, 450);
            tabControl1.TabIndex = 0;
            // 
            // tabPage1
            // 
            tabPage1.Controls.Add(n1TestBtn);
            tabPage1.Controls.Add(n1LogsRtb);
            tabPage1.Location = new Point(4, 29);
            tabPage1.Name = "tabPage1";
            tabPage1.Padding = new Padding(3);
            tabPage1.Size = new Size(792, 417);
            tabPage1.TabIndex = 0;
            tabPage1.Text = "N+1";
            tabPage1.UseVisualStyleBackColor = true;
            // 
            // n1TestBtn
            // 
            n1TestBtn.Location = new Point(70, 316);
            n1TestBtn.Name = "n1TestBtn";
            n1TestBtn.Size = new Size(94, 29);
            n1TestBtn.TabIndex = 1;
            n1TestBtn.Text = "Test N+1";
            n1TestBtn.UseVisualStyleBackColor = true;
            n1TestBtn.Click += n1TestBtn_Click;
            // 
            // n1LogsRtb
            // 
            n1LogsRtb.Location = new Point(6, 6);
            n1LogsRtb.Name = "n1LogsRtb";
            n1LogsRtb.Size = new Size(234, 304);
            n1LogsRtb.TabIndex = 0;
            n1LogsRtb.Text = "";
            // 
            // tabPage2
            // 
            tabPage2.Controls.Add(expAnlRtb);
            tabPage2.Controls.Add(rmvIndexBtn);
            tabPage2.Controls.Add(addIndexBtn);
            tabPage2.Controls.Add(multiBtn);
            tabPage2.Controls.Add(salaryBtn);
            tabPage2.Controls.Add(dptBtn);
            tabPage2.Controls.Add(emailBtn);
            tabPage2.Controls.Add(indexResultsDgv);
            tabPage2.Location = new Point(4, 29);
            tabPage2.Name = "tabPage2";
            tabPage2.Padding = new Padding(3);
            tabPage2.Size = new Size(792, 417);
            tabPage2.TabIndex = 1;
            tabPage2.Text = "Indexare";
            tabPage2.UseVisualStyleBackColor = true;
            // 
            // expAnlRtb
            // 
            expAnlRtb.Location = new Point(474, 76);
            expAnlRtb.Name = "expAnlRtb";
            expAnlRtb.Size = new Size(310, 254);
            expAnlRtb.TabIndex = 7;
            expAnlRtb.Text = "";
            // 
            // rmvIndexBtn
            // 
            rmvIndexBtn.Location = new Point(474, 41);
            rmvIndexBtn.Name = "rmvIndexBtn";
            rmvIndexBtn.Size = new Size(94, 29);
            rmvIndexBtn.TabIndex = 6;
            rmvIndexBtn.Text = "Fara Index";
            rmvIndexBtn.UseVisualStyleBackColor = true;
            rmvIndexBtn.Click += rmvIndexBtn_Click;
            // 
            // addIndexBtn
            // 
            addIndexBtn.Location = new Point(474, 6);
            addIndexBtn.Name = "addIndexBtn";
            addIndexBtn.Size = new Size(94, 29);
            addIndexBtn.TabIndex = 5;
            addIndexBtn.Text = "Cu Index";
            addIndexBtn.UseVisualStyleBackColor = true;
            addIndexBtn.Click += addIndexBtn_Click;
            // 
            // multiBtn
            // 
            multiBtn.Location = new Point(374, 336);
            multiBtn.Name = "multiBtn";
            multiBtn.Size = new Size(94, 29);
            multiBtn.TabIndex = 4;
            multiBtn.Text = "Multi";
            multiBtn.UseVisualStyleBackColor = true;
            multiBtn.Click += multiBtn_Click;
            // 
            // salaryBtn
            // 
            salaryBtn.Location = new Point(257, 336);
            salaryBtn.Name = "salaryBtn";
            salaryBtn.Size = new Size(94, 29);
            salaryBtn.TabIndex = 3;
            salaryBtn.Text = "Salariu";
            salaryBtn.UseVisualStyleBackColor = true;
            salaryBtn.Click += salaryBtn_Click;
            // 
            // dptBtn
            // 
            dptBtn.Location = new Point(122, 336);
            dptBtn.Name = "dptBtn";
            dptBtn.Size = new Size(110, 29);
            dptBtn.TabIndex = 2;
            dptBtn.Text = "Departament";
            dptBtn.UseVisualStyleBackColor = true;
            dptBtn.Click += dptBtn_Click;
            // 
            // emailBtn
            // 
            emailBtn.Location = new Point(8, 336);
            emailBtn.Name = "emailBtn";
            emailBtn.Size = new Size(94, 29);
            emailBtn.TabIndex = 1;
            emailBtn.Text = "Email";
            emailBtn.UseVisualStyleBackColor = true;
            emailBtn.Click += emailBtn_Click;
            // 
            // indexResultsDgv
            // 
            indexResultsDgv.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            indexResultsDgv.Location = new Point(8, 6);
            indexResultsDgv.Name = "indexResultsDgv";
            indexResultsDgv.RowHeadersWidth = 51;
            indexResultsDgv.Size = new Size(460, 324);
            indexResultsDgv.TabIndex = 0;
            // 
            // tabPage3
            // 
            tabPage3.Controls.Add(explAnlRtb);
            tabPage3.Controls.Add(totalEmpLbl);
            tabPage3.Controls.Add(offsetCheck);
            tabPage3.Controls.Add(pageSizeCmb);
            tabPage3.Controls.Add(pageInfoLbl);
            tabPage3.Controls.Add(nextBtn);
            tabPage3.Controls.Add(prevBtn);
            tabPage3.Controls.Add(employeeDgv);
            tabPage3.Location = new Point(4, 29);
            tabPage3.Name = "tabPage3";
            tabPage3.Padding = new Padding(3);
            tabPage3.Size = new Size(792, 417);
            tabPage3.TabIndex = 2;
            tabPage3.Text = "Paginare";
            tabPage3.UseVisualStyleBackColor = true;
            // 
            // explAnlRtb
            // 
            explAnlRtb.Location = new Point(481, 82);
            explAnlRtb.Name = "explAnlRtb";
            explAnlRtb.Size = new Size(303, 263);
            explAnlRtb.TabIndex = 7;
            explAnlRtb.Text = "";
            // 
            // totalEmpLbl
            // 
            totalEmpLbl.AutoSize = true;
            totalEmpLbl.Location = new Point(481, 357);
            totalEmpLbl.Name = "totalEmpLbl";
            totalEmpLbl.Size = new Size(50, 20);
            totalEmpLbl.TabIndex = 6;
            totalEmpLbl.Text = "label1";
            // 
            // offsetCheck
            // 
            offsetCheck.AutoSize = true;
            offsetCheck.Location = new Point(481, 52);
            offsetCheck.Name = "offsetCheck";
            offsetCheck.Size = new Size(71, 24);
            offsetCheck.TabIndex = 5;
            offsetCheck.Text = "Offset";
            offsetCheck.UseVisualStyleBackColor = true;
            offsetCheck.CheckedChanged += offsetCheck_CheckedChanged;
            // 
            // pageSizeCmb
            // 
            pageSizeCmb.FormattingEnabled = true;
            pageSizeCmb.Items.AddRange(new object[] { "10", "25", "50", "100" });
            pageSizeCmb.Location = new Point(481, 18);
            pageSizeCmb.Name = "pageSizeCmb";
            pageSizeCmb.Size = new Size(151, 28);
            pageSizeCmb.TabIndex = 4;
            pageSizeCmb.SelectedIndexChanged += pageSizeCmb_SelectedIndexChanged;
            // 
            // pageInfoLbl
            // 
            pageInfoLbl.AutoSize = true;
            pageInfoLbl.Location = new Point(208, 320);
            pageInfoLbl.Name = "pageInfoLbl";
            pageInfoLbl.Size = new Size(50, 20);
            pageInfoLbl.TabIndex = 3;
            pageInfoLbl.Text = "label1";
            // 
            // nextBtn
            // 
            nextBtn.Font = new Font("Segoe UI", 9F, FontStyle.Bold, GraphicsUnit.Point, 0);
            nextBtn.Location = new Point(381, 316);
            nextBtn.Name = "nextBtn";
            nextBtn.Size = new Size(94, 29);
            nextBtn.TabIndex = 2;
            nextBtn.Text = ">";
            nextBtn.UseVisualStyleBackColor = true;
            nextBtn.Click += nextBtn_Click;
            // 
            // prevBtn
            // 
            prevBtn.Font = new Font("Segoe UI", 9F, FontStyle.Bold, GraphicsUnit.Point, 0);
            prevBtn.Location = new Point(8, 316);
            prevBtn.Name = "prevBtn";
            prevBtn.Size = new Size(94, 29);
            prevBtn.TabIndex = 1;
            prevBtn.Text = "<";
            prevBtn.UseVisualStyleBackColor = true;
            prevBtn.Click += prevBtn_Click;
            // 
            // employeeDgv
            // 
            employeeDgv.ColumnHeadersHeightSizeMode = DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            employeeDgv.Location = new Point(6, 3);
            employeeDgv.Name = "employeeDgv";
            employeeDgv.RowHeadersWidth = 51;
            employeeDgv.Size = new Size(469, 307);
            employeeDgv.TabIndex = 0;
            // 
            // tabPage4
            // 
            tabPage4.Controls.Add(updateCacheBtn);
            tabPage4.Controls.Add(button1);
            tabPage4.Controls.Add(cachingTestBtn);
            tabPage4.Controls.Add(cachingMissesLbl);
            tabPage4.Controls.Add(cachingHitsLbl);
            tabPage4.Location = new Point(4, 29);
            tabPage4.Name = "tabPage4";
            tabPage4.Padding = new Padding(3);
            tabPage4.Size = new Size(792, 417);
            tabPage4.TabIndex = 3;
            tabPage4.Text = "Caching";
            tabPage4.UseVisualStyleBackColor = true;
            // 
            // button1
            // 
            button1.Location = new Point(350, 220);
            button1.Name = "button1";
            button1.Size = new Size(94, 70);
            button1.TabIndex = 3;
            button1.Text = "Prepared Statement Caching";
            button1.UseVisualStyleBackColor = true;
            button1.Click += button1_Click;
            // 
            // cachingTestBtn
            // 
            cachingTestBtn.Location = new Point(350, 126);
            cachingTestBtn.Name = "cachingTestBtn";
            cachingTestBtn.Size = new Size(94, 29);
            cachingTestBtn.TabIndex = 2;
            cachingTestBtn.Text = "Get";
            cachingTestBtn.UseVisualStyleBackColor = true;
            cachingTestBtn.Click += cachingTestBtn_Click;
            // 
            // cachingMissesLbl
            // 
            cachingMissesLbl.AutoSize = true;
            cachingMissesLbl.Location = new Point(371, 90);
            cachingMissesLbl.Name = "cachingMissesLbl";
            cachingMissesLbl.Size = new Size(50, 20);
            cachingMissesLbl.TabIndex = 1;
            cachingMissesLbl.Text = "label2";
            // 
            // cachingHitsLbl
            // 
            cachingHitsLbl.AutoSize = true;
            cachingHitsLbl.Location = new Point(371, 58);
            cachingHitsLbl.Name = "cachingHitsLbl";
            cachingHitsLbl.Size = new Size(50, 20);
            cachingHitsLbl.TabIndex = 0;
            cachingHitsLbl.Text = "label1";
            // 
            // tabPage5
            // 
            tabPage5.Controls.Add(bulkRunBtn);
            tabPage5.Controls.Add(timesRtb);
            tabPage5.Location = new Point(4, 29);
            tabPage5.Name = "tabPage5";
            tabPage5.Padding = new Padding(3);
            tabPage5.Size = new Size(792, 417);
            tabPage5.TabIndex = 4;
            tabPage5.Text = "Actualizari";
            tabPage5.UseVisualStyleBackColor = true;
            // 
            // bulkRunBtn
            // 
            bulkRunBtn.Location = new Point(8, 352);
            bulkRunBtn.Name = "bulkRunBtn";
            bulkRunBtn.Size = new Size(94, 29);
            bulkRunBtn.TabIndex = 1;
            bulkRunBtn.Text = "Test";
            bulkRunBtn.UseVisualStyleBackColor = true;
            bulkRunBtn.Click += bulkRunBtn_Click;
            // 
            // timesRtb
            // 
            timesRtb.Location = new Point(8, 6);
            timesRtb.Name = "timesRtb";
            timesRtb.Size = new Size(323, 340);
            timesRtb.TabIndex = 0;
            timesRtb.Text = "";
            // 
            // updateCacheBtn
            // 
            updateCacheBtn.Location = new Point(350, 161);
            updateCacheBtn.Name = "updateCacheBtn";
            updateCacheBtn.Size = new Size(94, 29);
            updateCacheBtn.TabIndex = 4;
            updateCacheBtn.Text = "Update";
            updateCacheBtn.UseVisualStyleBackColor = true;
            updateCacheBtn.Click += updateCacheBtn_Click;
            // 
            // Form1
            // 
            AutoScaleDimensions = new SizeF(8F, 20F);
            AutoScaleMode = AutoScaleMode.Font;
            ClientSize = new Size(800, 450);
            Controls.Add(tabControl1);
            Name = "Form1";
            Text = "Form1";
            Load += Form1_Load;
            tabControl1.ResumeLayout(false);
            tabPage1.ResumeLayout(false);
            tabPage2.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)indexResultsDgv).EndInit();
            tabPage3.ResumeLayout(false);
            tabPage3.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)employeeDgv).EndInit();
            tabPage4.ResumeLayout(false);
            tabPage4.PerformLayout();
            tabPage5.ResumeLayout(false);
            ResumeLayout(false);
        }

        #endregion

        private TabControl tabControl1;
        private TabPage tabPage1;
        private RichTextBox n1LogsRtb;
        private Button n1TestBtn;
        private TabPage tabPage2;
        private TabPage tabPage3;
        private TabPage tabPage4;
        private TabPage tabPage5;
        private Button multiBtn;
        private Button salaryBtn;
        private Button dptBtn;
        private Button emailBtn;
        private DataGridView indexResultsDgv;
        private Label pageInfoLbl;
        private Button nextBtn;
        private Button prevBtn;
        private DataGridView employeeDgv;
        private ComboBox pageSizeCmb;
        private Button cachingTestBtn;
        private Label cachingMissesLbl;
        private Label cachingHitsLbl;
        private Button bulkRunBtn;
        private RichTextBox timesRtb;
        private Button rmvIndexBtn;
        private Button addIndexBtn;
        private Button button1;
        private RichTextBox expAnlRtb;
        private CheckBox offsetCheck;
        private Label totalEmpLbl;
        private RichTextBox explAnlRtb;
        private Button updateCacheBtn;
    }
}
