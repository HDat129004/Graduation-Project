# 🚀 HOW TO COMMIT YOUR PROJECT TO GITHUB

## Your GitHub Repository
```
https://github.com/HDat129004/Graduation-Project.git
```

---

## ⚡ QUICK START (2 minutes)

### Windows Users: Just Click This! 👇
```
commit_to_github.bat
```
(Double-click file in Windows Explorer or run in Command Prompt)

### Linux/Mac Users: Run This 👇
```bash
bash commit_to_github.sh
```

### Then:
1. Enter GitHub credentials if asked
2. Wait for "Successfully pushed to GitHub!" message
3. Done! ✅

---

## 📋 What Will Happen

When you run the script:

```
[1/5] Initializing git repository... ✅
[2/5] Adding all files... ✅
[3/5] Committing changes... ✅
[4/5] Setting up remote repository... ✅
[5/5] Pushing to GitHub... ✅

Successfully pushed to GitHub!
Repository: https://github.com/HDat129004/Graduation-Project
```

---

## 🔍 What Gets Committed

### Code Files
- ✅ All Java source files (10+ files)
- ✅ All test files
- ✅ pom.xml (Maven config)

### Documentation  
- ✅ README.md
- ✅ All guides and documentation (15+ files)
- ✅ All verification documents

### Examples
- ✅ Calculator.java example
- ✅ Generated test examples

### Scripts
- ✅ All batch and shell scripts

### NOT Committed (ignored)
- ❌ target/ directory
- ❌ .idea/ directory
- ❌ .class files
- ❌ .jar files
- ❌ Temporary files

---

## ✅ Verification

After commit, verify on GitHub:

1. Go to: https://github.com/HDat129004/Graduation-Project
2. You should see:
   - All your files
   - Commit history
   - README.md displayed

---

## 🔐 GitHub Authentication

If prompted for credentials:

### Option 1: Use GitHub Token (Recommended)
1. Visit: https://github.com/settings/tokens
2. Click "Generate new token"
3. Select "repo" scope
4. Copy token
5. Paste token as password when prompted

### Option 2: Use GitHub Credentials
- Username: Your GitHub username
- Password: Your GitHub password (or token)

### Option 3: Configure Once
```bash
# Windows
git config --global credential.helper wincred

# Mac
git config --global credential.helper osxkeychain

# Linux
git config --global credential.helper store
```

---

## 🐛 Common Issues & Solutions

### "fatal: not a git repository"
**Problem:** Running from wrong directory
**Solution:** Run from project folder
```bash
cd C:\Users\Admin\Desktop\DATN\DoAnTN
commit_to_github.bat
```

### "fatal: remote origin already exists"
**Problem:** Remote already configured
**Solution:** Update existing remote
```bash
git remote set-url origin https://github.com/HDat129004/Graduation-Project.git
```

### "fatal: Authentication failed"
**Problem:** Wrong credentials
**Solution:** Use GitHub token instead of password

### "fatal: could not read Username"
**Problem:** Git not configured
**Solution:** Configure git first
```bash
git config --global user.name "Your Name"
git config --global user.email "your@email.com"
```

---

## 📊 After Successful Commit

### You Can Now:
- ✅ View files on GitHub
- ✅ Share link: https://github.com/HDat129004/Graduation-Project
- ✅ Clone to another computer
- ✅ Invite collaborators
- ✅ Track changes with git history

### Link to Share:
```
https://github.com/HDat129004/Graduation-Project
```

### To Clone Later:
```bash
git clone https://github.com/HDat129004/Graduation-Project.git
```

---

## 🎯 Recommended Process

### Step 1: Prepare (Already Done! ✅)
- ✅ All code complete
- ✅ All tests pass
- ✅ All documentation ready
- ✅ .gitignore configured

### Step 2: Commit (Do Now!)
```
Run: commit_to_github.bat (Windows)
  or
Run: bash commit_to_github.sh (Linux/Mac)
```

### Step 3: Verify
- Go to GitHub
- Check files are there
- Share link with advisor

---

## 📞 Need More Help?

### Read the Full Guide:
👉 [COMMIT_GUIDE.md](COMMIT_GUIDE.md)

### Manual Process:
```bash
cd C:\Users\Admin\Desktop\DATN\DoAnTN

# Initialize git (if needed)
git init

# Add all files
git add .

# Commit with message
git commit -m "Initial commit: DoAnTN - Automatic Unit Test Generator for Java"

# Add remote
git remote add origin https://github.com/HDat129004/Graduation-Project.git

# Set main branch
git branch -M main

# Push to GitHub
git push -u origin main
```

---

## ✨ Summary

| Step | Action | Time |
|------|--------|------|
| 1 | Open `commit_to_github.bat` | 10 sec |
| 2 | Double-click or run | 30 sec |
| 3 | Enter credentials | 30 sec |
| 4 | Wait for push | 30 sec |
| 5 | Verify on GitHub | 1 min |

**Total Time:** ~3 minutes

---

## 🚀 READY TO COMMIT!

👉 **Double-click:** `commit_to_github.bat` (Windows)

👉 **Or Run:** `bash commit_to_github.sh` (Linux/Mac)

That's it! Your project will be on GitHub in minutes! 🎉

---

**Status:** ✅ Ready to Commit
**Time:** 2-3 minutes
**Result:** Your project on GitHub ✅

