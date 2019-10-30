# You've added your first Readme file!
A README.md file is intended to quickly orient readers to what your project can do.  New to Markdown? [Learn more](http://go.microsoft.com/fwlink/p/?LinkId=524306&clcid=0x409)

## Edit this README and commit your change to a topic branch
In Git, branches are cheap.  You should use them whenever you're making changes to your repository.  Edit this file by clicking on the edit icon.

Then make some changes to this README file.

> Make some **edits** to _this_ blockquote

When you are done, click the dropdown arrow next to the save button - that will allow you to commit your changes to a new branch.

## Create a pull request to contribute your changes back into master
Pull requests are the way to move changes from a topic branch back into the master branch.

Click on the **Pull Requests** page in the **CODE** hub, then click "New Pull Request" to create a new pull request from your topic branch to the master branch.

When you are done adding details, click "Create Pull request". Once a pull request is sent, reviewers can see your changes, recommend modifications, or even push follow-up commits.

First time creating a pull request?  [Learn more](http://go.microsoft.com/fwlink/?LinkId=533211&clcid=0x409)

### Congratulations! You've completed the grand tour of the CODE hub!

# Next steps

If you haven't done so yet:
* [Install Visual Studio](http://go.microsoft.com/fwlink/?LinkId=309297&clcid=0x409&slcid=0x409)
* [Install Git](http://git-scm.com/downloads)

Then clone this repo to your local machine to get started with your own project.

Happy coding!

-----------------------------------
Thank you, I am login. Steven

---------------------------------------------------

Steps to install git and config your access:
1. Download and install a GIT from https://git-scm.com/download/win
2. Install the GIT to your local. I am installing the most simple options, no windows PATH change, not startup folder. 
3. Tell me your hotmail account so I can add you to the project;
4. Try to log into the project https://cancerdrug.visualstudio.com
5. And get familiar with the workspace. View code in CODE tab and manage tasks in WORK tab; You could drag tasks to different sprints or to backlog items;
6. Follow this link to create a "Personal access tokens", and keep the token as password for further use.
7. In the GIT command line, run command, input your hotmail account, and paste the previous token as password.
	git clone https://cancerdrug.visualstudio.com/DefaultCollection/_git/CancerDrug
	Cloning into 'CancerDrug'...
	Username for 'https://cancerdrug.visualstudio.com': steven_wang70@hotmail.com
	Password for 'https://steven_wang70@hotmail.com@cancerdrug.visualstudio.com':
	remote:
	remote:                    fTfs
	remote:                  fSSSSSSSs
	remote:                fSSSSSSSSSS
	remote: TSSf         fSSSSSSSSSSSS
	remote: SSSSSF     fSSSSSSST SSSSS
	remote: SSfSSSSSsfSSSSSSSt   SSSSS
	remote: SS  tSSSSSSSSSs      SSSSS
	remote: SS   fSSSSSSST       SSSSS
	remote: SS fSSSSSFSSSSSSf    SSSSS
	remote: SSSSSST    FSSSSSSFt SSSSS
	remote: SSSSt        FSSSSSSSSSSSS
	remote:                FSSSSSSSSSS npacking objects:  33% (1/3)
	remote:                  FSSSSSSs
	remote:                    FSFs    (TM)
	remote:
	remote:  Microsoft (R) Visual Studio (R) Team Foundation Server
	remote:
	Unpacking objects: 100% (3/3), done.
	Checking connectivity... done.

The TFS git does not support SSH. Normally it will ask for user and password when connecting to the server. You  could install a tool to store the user and password. See links:
https://gitcredentialstore.codeplex.com/
