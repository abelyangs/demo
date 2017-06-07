# Demo

demo to learn github.

(本仓库主要作为demo练习使用，和技术疑点记录)

Selenium-RC 最近已经大幅度地改进了性能，包括在单一线程的环境下。但是，多机器多线程测试对于长时间测试来说还是有相当大的益处的。利用足够的测试处理能力和测试的独立性，对于减少最长测试的时间是可行的。

虽然这些测试可以不必知道自己是在单机上顺序运行还是运行于整个机群，但Selenium Grid却不负责这些测试的并行执行，这些是由TestNG，Parallel JUnit和DeepTest for Ruby等完成的。


我们讨论过隔离性，以及开发Grid之前所面临的问题。我们想现在就把这个担子交给写测试的人，让他们来设计测试用例，以确保它们之间不会相互影响。当 然，这个问题在Gird产生之前就已经存在了。你不想让你的Selenium受其执行顺序的影响，那在每个测试执行之前要做一些数据初始化工作，执行这后 再清理掉。然而，这不是一个优雅的解决方案。理想情况下，你的Selenium test最好只了解这个应用的前端，但实际上，通过暴露一点数据给测试，会使针对具体的Scenarios写测试比较快且方便，而且由于只要较少的导航页 就可达到被测试页，运行时间会较少。嗯，看来有一点儿道理啦！但是不管怎么样，我们还是希望Grid能够支持这两种方式，不久前我们找到了一些方法可以在 数据库层隔离这些测试。虽然还只算是alpha版，但它可能会成为Grid的一部分，也可有是一个独立的项目。


Grid 只是提供运行脚本的环境，无法决定脚本以什么样的形式去跑（并行的方式，或者多环境的方式）。脚本以什么样的形式跑，由脚本本身和脚本的runner （一般用junit或者TestNG）的配置所决定.
并行和多环境的是怎么实现的。

 
第 一，并行。并行很简单。。脚本就是普通的脚本。然后如果你的runner支持并行运行的话。你就配置成并行。那运行起来就是并行的。。目前junit本身 不支持并行，一般都是用TestNG. 只要让TestNG 并行的运行测试，无论你的测试环境是由一个grid拖几个rc组成，还是 单一一个rc组成。。实际上 运行起来 测试就是并行的，唯一的区别是。如果用了grid的话，grid会把并发数量平均分配到不同的rc上去，然后每个rc会启动一个浏览器运行测试。而没有用 grid情况就是同一个rc直接跑多个，就是一个rc直接打开多个浏览器窗口运行多个测试。

 
第 二，多环境。 说多环境，这里也要澄清一下。 不是说给一个普通的测试脚本，丢给grid就可以自动实现多环境同时测试了。那是完全错误的。。这里需要几点。1. 一个普通脚本能测试一个某一种环境。所以如果你要测试多个环境就需要多个脚本。而这些脚本的区别仅在于setup的时候定义 DefaultSelenium("localhost",4444,"*firefox",Url); 这里。 因此两个测试方法也可以到同一个文件里面。写两个不同的方法，不同的定义DefaultSelenium部分，但调用同样的测试步骤。（就是 selenium.open...开始到selenium.stop...）. 这样一个文件下就算包含测试两个不同环境的测试方法了。
2. 让这两个测试方法，并行运行。。那又是testng的事情了。配置testng的suite。不细说。可以查看testng的文档。
3. grid配置的测试的rc环境，里面需要有刚才两个测试方法所需要的环境。
 
import static com.thoughtworks.selenium.grid.tools.ThreadSafeSeleniumSessionStorage.closeSeleniumSession;
import static com.thoughtworks.selenium.grid.tools.ThreadSafeSeleniumSessionStorage.session;
import static com.thoughtworks.selenium.grid.tools.ThreadSafeSeleniumSessionStorage.startSeleniumSession;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.thoughtworks.selenium.Selenium;

public class Sample {
 private Selenium selenium;
 
    @BeforeMethod
    @Parameters({"seleniumHost", "seleniumPort", "browser", "webSite"})
    protected void startSession(String seleniumHost, int seleniumPort, String browser, String webSite) throws Exception {
        startSeleniumSession(seleniumHost, seleniumPort, browser, webSite);
        selenium = session();
        selenium.setTimeout("120000");
    }
    @AfterMethod
    protected void closeSession() throws Exception {
        closeSeleniumSession();
    }
 
 @Test
 public void test1() {
  selenium.open("/");
  selenium.type("q", "test1");
  selenium.click("btnG");
  selenium.waitForPageToLoad("30000");
 }
 
 【略】
