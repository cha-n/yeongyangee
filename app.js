/*jshint esversion: 6 */
var express = require('express');
var session = require('express-session');
var bodyParser = require('body-parser');
var MySQLStore = require('express-mysql-session')(session);
var bkfd2Password = require("pbkdf2-password");
var passport = require('passport');
var LocalStrategy = require('passport-local').Strategy;
var hasher = bkfd2Password();
var mysql      = require('mysql');
var conn = mysql.createConnection({
  host     : 'localhost',
  user     : 'root',
  password : '961115',
  database : 'food',
   multipleStatements: true
});
conn.connect();
var app=express();
app.use(express.static('public'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended:true}));
app.use(session({
  secret: 'awefat',
  resave: false,
  saveUninitialized: false,
  cookie : { secure : false, maxAge : (4 * 60 * 60 * 1000) },
  store: new MySQLStore({
    host:'localhost',
    port:3306,
    user:'root',
    password:'961115',
    database:'food',
  })
}));
app.use(passport.initialize()); //app.use를 하게 되면 passport를 초기화하고 앱에서 passport를 사용하도록 setting
app.use(passport.session());  //passport를 이용해서 인증 작업을 할 때  session을 사용하도록.

app.locals.pretty = true;
app.set('views','./views');
app.set('view engine','jade');


//로그인, 회원가입
app.get('/auth/logout', function(req,res){
  req.logout();
  req.session.save(function(){
    res.redirect('/welcome');
  });
});
//passport를 이용해서 user정보에 접근하는 것이 적당
app.get('/welcome', function(req,res){
  if (req.user && req.user[0].username){
    console.log('==============register->login 성공==========');
    res.send(`
        <h1>Hello, ${req.user[0].username}</h1>
        <a href="/auth/logout">Logout</a>
      `);
  }else{
    res.send(`
    <h1>Welcome</h1>
    <ul>
      <li><a href="/auth/register">Register</a></li>
    </ul>
    `);
  }
});

/*로그인 성공시 사용자 정보를 Session에 저장한다*/
passport.serializeUser(function(user, done) {
  console.log('serializeUser', user.username);
  done(null, user.authID);  //username값이 done 함수로 session에 등록됨.
  console.log('serialize Done');
});
/*인증 후, 페이지 접근시 마다 사용자 정보를 Session에서 읽어옴.*/
passport.deserializeUser(function(ID, done) {
  console.log('deserializeUser', ID);
  var sql = 'SELECT * FROM users WHERE authID=?';
  conn.query(sql, [ID], function(err, results){
    if(err){
      console.log(err);
      done('There is no user.');
    } else {
      //console.log(results);
      done(null, results);
    }
  });
});

passport.use(new LocalStrategy(
  function(username, password, done){
      //console.log('Local Strategy'+username);
      var uname = username;
      var pwd = password;
      var sql = 'SELECT * FROM users WHERE authID=?';
      conn.query(sql, ['local:'+uname], function(err, results){
        console.log(results);
        if(err){
          return done('There is no user.');
        }
        var user = results[0];
        return hasher({password:pwd, salt:user.salt}, function(err, pass, salt, hash){
              if (hash === user.password){//사용자가있다
                return done(null, user); //로그인 절차가 끝났는데 성공함. 로그인한 사용자에 대한 객체로 사용될 ㅣ것.
              }else{//사용자가 없다
                return done(null, false);  //로그인 절차가 끝났는데 실패함.
              }
            });
      });
  }
));

var users = [    //전역으로 배열 생성.
  {
    name:'v',
    username:'username',  //비번 : 111
    password:'2q7MGHZLVtPOigWe5714ai52+OC9vo/GKJJ1oPO2x/2GIxax4EgfQf9KxFCX32ew+RXZZ0pW5aUoOFNll1JP8BO3V0Nf2Lw2vvuRLLK8JfD5Msl3m/Jje4VpCFvZL6Iq6fJ7DxfjetnUwaPkJahP+P1aEFZP6OLp0EALa2MdfKQ=',
    salt: 'VLCYhQZsc/AyFIXZE15goHb6ib5JnimGiUVKIORvwOAiAjM1e08YAIncas0xbpfCvtFKKrqOXBUJz1Pajib2zg==',
    age: 22,
    gender: 'f',
    authID:'local:username'
  }
];

// 로그인
app.get('/auth/login', function(req,res){
  var output = `
  <h1>Login</h1>
  <form action="/auth/login" method="post">
    <p>
      <input type="text" name="username" placeholder="ID">
    </p>
    <p>
      <input type="password" name="password" placeholder="password">
    </p>
    <p>
      <input type="submit">
    </p>
  </form>
  `;
  res.send(output);
});

app.get('/auth/success',function(req,res){
  res.json({
              result: "t",
              msg: '로그인 성공!'
          });
})
app.get('/auth/failure',function(req,res){
  res.json({
              result: "f",
              msg: '로그인 실패!'
          });
})

app.post('/auth/login',function(req,res){
  var username = req.body.username;
  var pwd=req.body.password;
  var sql = 'select * from users where username=?';
  conn.query(sql, username, function(err,results){
    if (results.length==0)
    {
      console.log("없는 아이디");
      res.redirect('/auth/failure');
    }
    else{
      return hasher({password:pwd, salt:results[0].salt}, function(err, pass, salt, hash){
        if (hash==results[0].password){
          console.log("로그인 성공");
          res.redirect('/auth/success');
        }
        else {
          console.log("비밀번호 틀림");
          res.redirect('/auth/failure');
        }
      });
    }
  });
});

// 회원가입
app.get('/auth/register', function(req, res){
  var output = `
  <h1>Register</h1>
  <form action="/auth/register" method="post">
    <p>
      <input type="text" name="name" placeholder="name">
    </p>
    <p>
      <input type="text" name="username" placeholder="username">
    </p>
    <p>
      <input type="password" name="password" placeholder="password">
    </p>
    <p>
      <input type="text" name="age" placeholder="age">
    </p>
    <p>
      <input type="text" name="gender" placeholder="gender(m/f)">
    </p>
    <p>
      <input type="submit">
    </p>
  </form>
  `;
  res.send(output);
});


app.post('/auth/register', function(req, res){
  //console.log(req.body);
  hasher({password:req.body.password}, function(err, pass, salt, hash){
    var user = {
      username:req.body.username,
      password:hash,//hash로 대체함
      name: req.body.name,
      age:req.body.age,
      gender:req.body.gender,
      salt:salt,//만든 salt값도 같이 저장함
      authID:'local:'+req.body.username
    };
    var sql = 'INSERT IGNORE INTO users SET ?';
    conn.query(sql, user, function(err, results){
      if(err){
        console.log(err);
        res.status(500);
      }else{
        //회원가입후 바로 로그인 하기 위한 코드임
        req.login(user, function(err){//회원가입이 되고 바로 동시에 로그인 하기 위함
          req.session.save(function(){
            res.redirect('/welcome');
          });
        });
      }
    });//두번째에 user를 주면 알아서 authId = ~~ 등으로 들어감
  });
});


//부족한 영양소별 추천음식 사진
app.get('/pic_tan', function(req,res){
  res.send('<img src="/tan/1.jpg">, <img src="/tan/2.jpg">,<img src="/tan/3.jpg">, <img src="/tan/4.jpg">, <img src="/tan/5.jpg">, <img src="/tan/6.jpg">, <img src="/tan/7.jpg">, <img src="/tan/8.jpg">, <img src="/tan/9.jpg">, <img src="/tan/10.jpg">, <img src="/tan/11.jpg">, <img src="/tan/12.jpg">, <img src="/tan/13.jpg">, <img src="/tan/14.jpg">, <img src="/tan/15.jpg">')
})
app.get('/pic_dan', function(req,res){
  res.send('<img src="/dan/1.jpg">, <img src="/dan/2.jpg">,<img src="/dan/3.jpg">, <img src="/dan/4.jpg">, <img src="/dan/5.jpg">, <img src="/dan/6.jpg">, <img src="/dan/7.jpg">, <img src="/dan/8.jpg">, <img src="/dan/9.jpg">, <img src="/dan/10.jpg">, <img src="/dan/11.jpg">, <img src="/dan/12.jpg">, <img src="/dan/13.jpg">, <img src="/dan/14.jpg">, <img src="/dan/15.jpg">')
})
app.get('/pic_gi', function(req,res){
  res.send('<img src="/gi/1.jpg">, <img src="/gi/2.jpg">,<img src="/gi/3.jpg">, <img src="/gi/4.jpg">, <img src="/gi/5.jpg">, <img src="/gi/6.jpg">, <img src="/gi/7.jpg">, <img src="/gi/8.jpg">, <img src="/gi/9.jpg">, <img src="/gi/10.jpg">, <img src="/gi/11.jpg">, <img src="/gi/12.jpg">, <img src="/gi/13.jpg">, <img src="/gi/14.jpg">, <img src="/gi/15.jpg">')
})
app.get('/tan', function(req,res){
  var sql = 'SELECT * FROM rec_tan';
  conn.query(sql, function(err, rows, fields){
    res.send(JSON.stringify(rows));
  })
})
app.get('/dan', function(req,res){
  var sql = 'SELECT * FROM rec_dan';
  conn.query(sql, function(err, rows, fields){
    res.send(JSON.stringify(rows));
  })
})
app.get('/gi', function(req,res){
  var sql = 'SELECT * FROM rec_gi';
  conn.query(sql, function(err, rows, fields){
    res.send(JSON.stringify(rows));
  })
})
app.get('/rec_tan', function(req,res){
  var sql = 'SELECT * FROM rec_tan';
  conn.query(sql, function(err, rows, fields){
    res.send(JSON.stringify(rows),'<img src="/tan/1.jpg">');
  })
})


app.get('/users', (req, res) => {
   console.log('who get in here/users');
   res.json(users)
});


//음식 데이터
app.get(['/food', '/food/:name'], function(req,res){
  var sql = 'SELECT * FROM food';
  conn.query(sql, function(err, rows, fields){
    var name = req.params.name;
    if (name){
      var sql = 'SELECT * FROM food WHERE 식품이름=?';
      conn.query(sql, [name], function(err, food, fields){
        if (err){
          console.log(err);
          res.status(500).send('Internal Server Error');
        }else{
          res.render('food_form', {foods:rows, food:food[0]});
        }
      });
    }else{
      res.render('food_form', {foods:rows});
    }
  });
});


//음식 리스트
//db: food
app.get('/foods', function(req,res){
  var sql = 'SELECT * FROM food ';

  conn.query(sql, function(err, rows, fields){
    res.send(JSON.stringify(rows));
  })
})


//성별,나이대별 권장섭취량
//db: eat
app.get('/eat', function(req,res){
  var sql = 'SELECT 성별,나이,에너지_kcal,탄수화물_g,단백질_g,지방_g,당류_g,나트륨_mg FROM eat ';
//  var sql = 'SELECT * FROM food where 번호=9';
  conn.query(sql, function(err, rows, fields){
    res.send(JSON.stringify(rows));
    //console.log(JSON.stringify(rows));
  })
})


//혈압.임신.비만.혈당 등 분야별 동영상 url
//db :recommend
app.get('/url', function(req,res){
  var sql = 'SELECT * FROM recommend';
  conn.query(sql, function(err, rows, fields){
    res.send(JSON.stringify(rows));
    //console.log(JSON.stringify(rows));
  })
})


//117.16.244.117:2001/cal/post/my_food
//my_food에 음식 올리기
app.post('/cal/post/my_food', function(req,res){
  console.log(req.body);
  for (var i in req.body.test){
    console.log(req.body.test[i]);
  }
  var sql,sql2;

  for (var i in req.body.test){
    var list = {
      username:req.body.test[i].username,
      year:req.body.test[i].year,
      month:req.body.test[i].month,
      day:req.body.test[i].day,
      hour:req.body.test[i].hour,
      min:req.body.test[i].min,
      foodname:req.body.test[i].foodname
    };
    sql = 'INSERT INTO cal SET ? ';
    conn.query(sql, list, function(err,results){
      if(err){
        console.log(err);
         res.status(500);
      }else{
        console.log("list1");
        console.log(list);
        // res.redirect('/cal/get');
      }
    });
    var list2 = {
          username:req.body.test[i].username,
          foodname:req.body.test[i].foodname,
          Kcal:req.body.test[i].Kcal,
          Tan:req.body.test[i].Tan,
          Dan:req.body.test[i].Dan,
          Gi:req.body.test[i].Gi,
          Dang:req.body.test[i].Dang,
          Na:req.body.test[i].Na,
          year:req.body.test[i].year,
          month:req.body.test[i].month,
          day:req.body.test[i].day,
          hour:req.body.test[i].hour,
          min:req.body.test[i].min
    };
    sql2 = 'INSERT INTO my_food SET ?';
    conn.query(sql2, list2, function(err,results){
      if(err){
        console.log(err);
         res.status(500);
      }else{
        console.log("list2");
        console.log(list2);
        // res.redirect('/cal/get');
      }
    });
  }
  res.redirect('/cal/get');
});


//캘린더 보내는 거   db:cal
//117.16.244.117:2001/cal/post
app.post('/cal/post', function(req,res){
//array쓸때
  console.log(req.body);
  for (var i in req.body.test){
    console.log(req.body.test[i]);
  }
  var sql;
  for (var i in req.body.test){
    var list = {
      username:req.body.test[i].username,
      year:req.body.test[i].year,
      month:req.body.test[i].month,
      day:req.body.test[i].day,
      hour:req.body.test[i].hour,
      min:req.body.test[i].min,
      foodname:req.body.test[i].foodname
    };
    sql = 'INSERT INTO cal SET ?';
    conn.query(sql, list, function(err,results){
      if(err){
        console.log(err);
         res.status(500);
      }else{
        console.log(list);
      }
    });
  };
  res.redirect('/cal/get');
});

//117.16.244.117:2001/cal/get
//캘린더 get  db:cal x food 식품영양정보  가져옴
// my_food (사용자 foodDB)에서 식품영양정보 가져옴
app.get('/cal/get', function(req,res){
var sql = 'SELECT c.username, c.year, c.month, c.day, c.hour, c.min, c.foodname, f.열량_kcal, f.탄수화물_g, f.단백질_g, f.지방_g FROM cal c, food f WHERE c.foodname=f.식품이름 ORDER BY c.year, c.month, c.day, c.hour, c.min;'
+ 'SELECT c.username, c.year, c.month, c.day, c.hour, c.min, c.foodname, m.Kcal AS 열량_kcal, m.Tan AS 탄수화물_g , m.Dan As 단백질_g, m.Gi AS 지방_g FROM cal c, my_food m WHERE c.username=m.username AND c.foodname=m.foodname ORDER BY c.year, c.month, c.day, c.hour, c.min;';

   conn.query(sql, function(err, rows, fields){
          res.send(rows[0].concat(rows[1]));
      })
})

// 달력db에서 데이터 삭제
app.post('/cal/delete',function(req,res){
  console.log(req.body);
  var uname=req.body.username;
  var h=req.body.hour;
  var m=req.body.min;
  var fname=req.body.foodname;
  var sql = 'delete from cal where username=? and hour=? and min=? and foodname=?';
  conn.query(sql, [uname,h,m,fname], function(err, results){
    if (err){
      console.log(err);
      res.status(500).send('Internal Server Error');
    }else{
      console.log('삭제완료');
      res.redirect('/main2');
    }
  });
})

// 달력 db 해당 날짜 정보 삭제
app.post('/cal/reset',function(req,res){
  console.log(req.body);
  var uname=req.body.username;
  var m=req.body.month;
  var d=req.body.day;
  console.log(uname);
  var sql = 'delete from cal where username=? and month=? and day=?';
  conn.query(sql, [uname,m,d], function(err, results){
    if (err){
      console.log(err);
      res.status(500).send('Internal Server Error');
    }else{
      console.log('삭제완료');
      res.redirect('/main2');
    }
  });
})

var id;
app.post('/main', function(req,res){
  id=req.body.username;
  console.log("/main");
  console.log(id);
})
app.get('/main', function(req,res){
  var sql = 'SELECT u.username, e.에너지_kcal, e.탄수화물_g, e.단백질_g, e.지방_g, e.당류_g, e.나트륨_mg FROM eat e, users u WHERE u.age=e.나이 and u.gender=e.성별'
  conn.query(sql, function(err, rows, fields){
    res.send(rows);
  })
})

app.get('/main2', function(req,res){
  var sql = ' SELECT c.username, c.year, c.month, c.day, sum(f.열량_kcal) AS 열량_kcal, sum(f.탄수화물_g) AS 탄수화물_g, sum(f.단백질_g) AS 단백질_g, sum(f.지방_g) AS 지방_g, sum(f.당류_g) AS 당류_g, sum(나트륨_mg) AS 나트륨_mg FROM cal c, food f WHERE c.foodname=f.식품이름 GROUP BY c.username, c.year, c.month, c.day'
  conn.query(sql, function(err, rows, fields){
    res.send(rows);
  })
})
app.listen(2001, function() {
  console.log('Connected, 2001port!');
});
