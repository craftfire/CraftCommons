CraftCommons
============
Commons library by CraftFire which features a data source manager, encryption methods, and more.

Visit our [website][Website] or get support on our [forum thread][Forums].  
Track and submit issues and bugs on our [issue tracker][Issues].

[![Follow us on Twitter][Twitter Logo]][Twitter][![Like us on Facebook][Facebook Logo]][Facebook][![Donate][Donate Logo]][Donate]

## The License
CraftCommons is licensed under the [GNU Lesser General Public License Version 3][License].  
Copyright (c) 2012-2013, CraftFire <<http://www.craftfire.com/>>  
[![][Author Logo]][Website]

## Getting the Source
The latest and greatest source can be found on [GitHub].  
Download the latest builds from [Jenkins]. [![Build Status](http://build.craftfire.com/job/CraftCommons/badge/icon)][Jenkins]  
View the latest [Javadoc].

## Compiling the Source
CraftCommons uses Maven to handle its dependencies.

* Install [Maven 2 or 3](http://maven.apache.org/download.html)  
* Checkout this repo and run: `mvn clean install`

## Using with Your Project
For those using [Maven](http://maven.apache.org/download.html) to manage project dependencies, simply include the following in your pom.xml:

    <dependency>
        <groupId>com.craftfire</groupId>
        <artifactId>craftcommons</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>

If you do not already have repo.craftfire.com in your repository list, you will need to add this also:

    <repository>
        <id>craftfire-repo</id>
        <url>http://repo.craftfire.com</url>
    </repository>

## Coding and Pull Request Conventions
* Generally follow the Oracle coding standards.
* No tabs, use spaces for indentation.
* No trailing whitespaces on new lines.
* 200 column limit for readability.
* Pull requests must compile, work, and be formatted properly.
* Sign-off on ALL your commits - this indicates you agree to the terms of our license.
* No merges should be included in pull requests unless the pull request's purpose is a merge.
* Number of commits in a pull request should be kept to *one commit* and all additional commits must be *squashed*.
* You may have more than one commit in a pull request if the commits are separate changes, otherwise squash them.

**Please follow the above conventions if you want your pull request(s) accepted.**

[Author Logo]: http://cdn.craftfire.com/img/logo/craftfire_150x38.png
[License]: http://www.gnu.org/licenses/lgpl.html
[Website]: http://www.craftfire.com
[Forums]: http://forums.spout.org/threads/3338/
[GitHub]: https://github.com/craftfire/craftcommons
[Jenkins]: http://build.craftfire.com/job/craftcommons
[Issues]: http://issues.craftfire.com
[Twitter]: http://twitter.com/craftfiredev
[Facebook]: http://facebook.com/craftfire
[Donate]: https://www.paypal.com/cgi-bin/webscr?hosted_button_id=4K4LNLGDM9T6Y&item_name=CraftCommons+donation+%28from+github.com%29&cmd=_s-xclick
