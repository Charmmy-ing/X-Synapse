<script setup>
import {ref,computed} from 'vue'
import {Home,Compass,FileText,Grid2X2,Bookmark,UserRound,Search,Bell,ChevronDown,PenLine,Plus,Eye,MessageCircle,Heart,ArrowRight,Tag,Sparkles,Zap,MoreHorizontal,X} from 'lucide-vue-next'

const active=ref('首页'),tab=ref('推荐'),keyword=ref(''),liked=ref(new Set()),searchOpen=ref(false),mx=ref(50),my=ref(50)
const nav=[['首页',Home],['发现',Compass],['文章',FileText],['分类',Grid2X2],['我的收藏',Bookmark],['个人中心',UserRound]]
const articles=ref([
{id:1,author:'charmmy',time:'2小时前',title:'X-Synapse 项目开发记录：从需求到上线',desc:'记录一个真实项目从需求分析、技术选型，到 Spring Boot、Redis、MyBatis-Plus 与前端联调的全过程。',tags:['Java','Spring Boot','MyBatis-Plus','项目实战'],img:'https://images.unsplash.com/photo-1498050108023-c5249f4df085?auto=format&fit=crop&w=900&q=85',views:1250,comments:36,likes:128},
{id:2,author:'Tech分享官',time:'5小时前',title:'Java 后端开发基础：Spring Boot 4 新特性解析',desc:'从工程实践的角度梳理 Spring Boot 4 的主要变化，以及它们对后端项目结构和开发体验的影响。',tags:['Java','Spring Boot','后端开发'],img:'https://images.unsplash.com/photo-1555066931-4365d14bab8c?auto=format&fit=crop&w=900&q=85',views:856,comments:22,likes:64},
{id:3,author:'算法小白',time:'8小时前',title:'LeetCode 100 题学习计划（持续更新）',desc:'记录刷题过程中的思路、代码实现与复杂度分析，把零散的算法知识整理成自己的知识体系。',tags:['算法','LeetCode','数据结构与算法'],img:'https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=900&q=85',views:632,comments:18,likes:42}
])
const tags=['Java','Spring Boot','MyBatis-Plus','Redis','Linux','Docker','算法','LeetCode','AI','Agent','项目实战','学习笔记']
const filtered=computed(()=>{let q=keyword.value.trim().toLowerCase();return q?articles.value.filter(a=>[a.title,a.desc,a.author,...a.tags].join(' ').toLowerCase().includes(q)):articles.value})
function like(id){let s=new Set(liked.value);s.has(id)?s.delete(id):s.add(id);liked.value=s}
function move(e){let r=e.currentTarget.getBoundingClientRect();mx.value=(e.clientX-r.left)/r.width*100;my.value=(e.clientY-r.top)/r.height*100}
</script>

<template>
<div class="app" @mousemove="move">
  <div class="orb o1"></div><div class="orb o2"></div><div class="grid"></div>

  <aside class="side">
    <div class="brand"><div class="logo"><i></i><i></i><b></b></div><div><strong>X-Synapse</strong><small>连接思考 · 共享价值</small></div></div>
    <div class="space"><span></span> Personal Space <ChevronDown :size="13"/></div>
    <div class="label">WORKSPACE</div>
    <nav>
      <button v-for="[name,Icon] in nav" :key="name" :class="{on:active===name}" @click="active=name">
        <span class="navicon"><component :is="Icon" :size="18"/></span>{{name}}<em v-if="name==='文章'">32</em>
      </button>
    </nav>
    <div class="sidebottom">
      <div class="create-tip"><div><Sparkles :size="15"/></div><span><b>Keep creating.</b><small>让想法产生连接</small></span></div>
      <small>v2.0 · X-Synapse　© 2026</small>
    </div>
  </aside>

  <main>
    <header>
      <div class="crumb">Workspace <b>/</b> <strong>首页</strong></div>
      <button class="searchbar" @click="searchOpen=true"><Search :size="17"/>搜索任何内容... <kbd>⌘ K</kbd></button>
      <div class="actions"><button class="bell"><Bell :size="19"/><i></i></button><hr/><button class="user"><img src="https://i.pravatar.cc/80?img=47"/>charmmy <ChevronDown :size="14"/></button></div>
    </header>

    <div v-if="searchOpen" class="modal" @click.self="searchOpen=false">
      <div class="searchpanel"><div><Search :size="19"/><input v-model="keyword" autofocus placeholder="搜索文章、用户、分类..."/><button @click="searchOpen=false"><X :size="18"/></button></div><p><Sparkles :size="14"/> 试试搜索 Java、Redis、算法、AI、Agent</p></div>
    </div>

    <div class="layout">
      <section>
        <div class="hero">
          <div class="heroimg"></div><div class="heroglow" :style="{left:mx+'%',top:my+'%'}"></div><div class="herogrid"></div>
          <div class="particle p1"></div><div class="particle p2"></div><div class="particle p3"></div>
          <div class="heroText"><div class="badge"><span/> THE KNOWLEDGE NETWORK</div><h1>X-Synapse</h1><h2>连接思考 <i>·</i> 共享价值</h2><p>记录你的思考，分享你的见解，让每一次知识沉淀，都成为下一次连接的起点。</p>
          <div class="heroBtns"><button class="primary"><PenLine :size="16"/>开始创作 <ArrowRight :size="14"/></button><button class="ghost">探索内容</button></div></div>
          <div class="metrics"><div><small>COMMUNITY</small><b>2,486</b><span>活跃创作者</span></div><div><small>ARTICLES</small><b>12.8K</b><span>知识文章</span></div></div>
        </div>

        <div class="feed">
          <div class="feedhead"><div><small class="kicker">●　DISCOVER</small><h2>发现内容</h2></div><div class="tabs"><button v-for="x in ['推荐','最新','热门','关注']" :class="{on:tab===x}" @click="tab=x">{{x}}</button></div></div>
          <article v-for="(a,i) in filtered" :key="a.id" class="article" :style="{'--d':i*80+'ms'}">
            <div class="articlebody"><div class="author"><img :src="`https://i.pravatar.cc/64?img=${a.id+46}`"/><div>{{a.author}} <span>· {{a.time}}</span></div><b v-if="a.id===1">ORIGINAL</b><button><MoreHorizontal :size="18"/></button></div>
            <h3>{{a.title}}</h3><p>{{a.desc}}</p><div class="tags"><span v-for="t in a.tags"><Tag :size="10"/>{{t}}</span></div>
            <div class="stats"><span><Eye :size="14"/>{{a.views}}</span><span><MessageCircle :size="14"/>{{a.comments}}</span><button :class="{liked:liked.has(a.id)}" @click="like(a.id)"><Heart :size="14" :fill="liked.has(a.id)?'currentColor':'none'"/>{{a.likes+(liked.has(a.id)?1:0)}}</button></div></div>
            <div class="thumb"><img :src="a.img"/></div>
          </article>
        </div>
      </section>

      <aside class="right">
        <div class="profile"><div class="cover"><span></span><i></i></div><img src="https://i.pravatar.cc/120?img=47"/><label>● Online</label><h3>charmmy</h3><p>Building ideas into reality.</p><div class="profileStats"><div><b>12</b><small>关注</small></div><div><b>8</b><small>粉丝</small></div><div><b>32</b><small>文章</small></div></div></div>
        <div class="quick"><div class="quicktitle">QUICK ACTIONS <Zap :size="13"/></div>
          <button><span class="q blue"><PenLine :size="16"/></span><div><b>发布文章</b><small>分享你的想法与经验</small></div><ArrowRight :size="15"/></button>
          <button><span class="q green"><Plus :size="16"/></span><div><b>创建分类</b><small>管理你的兴趣领域</small></div><ArrowRight :size="15"/></button>
          <button><span class="q yellow"><Bookmark :size="16"/></span><div><b>我的收藏</b><small>保存有价值的内容</small></div><ArrowRight :size="15"/></button>
        </div>
        <div class="card"><div class="cardtitle"><div><small class="kicker">●　TRENDING</small><h3>热门标签</h3></div><button>全部 <ArrowRight :size="12"/></button></div><div class="hot"><span v-for="(t,i) in tags" :class="{hotone:i<3}"><Tag :size="10"/>{{t}}</span></div></div>
        <div class="card recent"><div class="cardtitle"><div><small class="kicker">●　HISTORY</small><h3>最近访问</h3></div></div><button v-for="a in articles"><i/>{{a.title}}</button></div>
      </aside>
    </div>
  </main>
</div>
</template>